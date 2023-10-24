package main

import (
	"sync"
	"time"
)

type Participant struct {
	lastBuy        time.Time
	canParticipate bool
	currentBet     int
	thingsBought   int
}

type Seller struct {
	maxThings         int
	currentThingCount int
	currentThing      *Purchase
	suspension        int
	barrier           *Barrier
}

type Purchase struct {
	mutex        sync.Mutex
	currentValue int
	baseValue    int
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

func (p *Participant) buy(s *Seller) {
	waitChan := make(chan int, 0)
	for {
		pr := s.provide()
		if pr == nil {
			return
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
			}
		} else {
			select {
			case <-waitChan:
			default:
				p.canParticipate = true
			}
		}
	}
}

func auction() {

}
