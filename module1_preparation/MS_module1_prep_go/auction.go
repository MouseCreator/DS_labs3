package main

import (
	"fmt"
	"strconv"
	"sync"
	"time"
)

type Participant struct {
	lastBuy        time.Time
	canParticipate bool
	thingsBought   int
	uprise         int
	name           string
}

type Seller struct {
	maxThings         int
	currentThingCount int
	currentThing      *Purchase
	barrier           *Barrier
}

type Purchase struct {
	mutex        sync.Mutex
	currentValue int
	baseValue    int
}

func (pr *Purchase) changePriceBy(delta int) int {
	defer pr.mutex.Unlock()
	pr.mutex.Lock()
	pr.currentValue = pr.currentValue + delta
	return pr.currentValue
}

func (s *Seller) next() {
	s.currentThingCount++
	if s.currentThingCount == s.maxThings {
		s.currentThing = nil
	}
	s.currentThing = &Purchase{sync.Mutex{}, 10, 10}
}
func (s *Seller) provide() *Purchase {
	s.barrier.await()
	return s.currentThing
}

func (s *Seller) finishBets() {
	s.barrier.await()
}

func (p *Participant) buy(s *Seller, group *sync.WaitGroup) {
	waitChan := make(chan int)
	for {
		s.next() //FIX
		pr := s.provide()
		if pr == nil {
			break
		}
		if p.canParticipate {
			currentTime := time.Now()
			timeSinceLastCheck := currentTime.Sub(p.lastBuy)
			secondsSinceLastCheck := int(timeSinceLastCheck.Seconds())
			if secondsSinceLastCheck > 5 {
				p.canParticipate = false
				waitChan <- 0
				waitChan <- 1
				waitChan <- 2
				continue
			}
			myBet := 0
			for i := 0; i < p.uprise; i++ {
				myBet = pr.changePriceBy(10)
				time.Sleep(time.Second / 2)
			}
			s.finishBets()
			if pr.currentValue == myBet {
				p.thingsBought++
				fmt.Println(p.name + " bought a thing for " + strconv.Itoa(myBet))
			}
		} else {
			select {
			case <-waitChan:
			default:
				p.canParticipate = true
			}
			s.finishBets()
		}
	}
	group.Done()
}

func main() {
	wg := sync.WaitGroup{}
	wg.Add(3)
	seller := Seller{13, 0, nil, makeBarrier(3)}
	p1 := Participant{time.Now(), true, 0, 0, "P0"}
	p2 := Participant{time.Now(), true, 0, 1, "P1"}
	p3 := Participant{time.Now(), true, 0, 2, "P2"}

	go p1.buy(&seller, &wg)
	go p2.buy(&seller, &wg)
	go p3.buy(&seller, &wg)

	wg.Wait()

}
