package main

/*
Extra task
Using trap to get out of the plane
Using terminal to get in the plane
*/
import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type Airplane struct {
	id       int
	capacity int
	rangeVal int
}

func newAirplane(id, capacity, rangeVal int) *Airplane {
	return &Airplane{id, capacity, rangeVal}
}

type Communication struct {
	terminalSemaphore *Semaphore
	trapSemaphore     *Semaphore
}

func newCommunication(terminalCapacity, trapCapacity int) *Communication {
	return &Communication{
		terminalSemaphore: NewSemaphore(terminalCapacity),
		trapSemaphore:     NewSemaphore(trapCapacity),
	}
}

func (c *Communication) communicateTrap() {
	c.communicate(c.trapSemaphore)
}

func (c *Communication) communicateTerminal() {
	c.communicate(c.terminalSemaphore)
}

func (c *Communication) communicate(semaphore *Semaphore) {
	semaphore.acquire()
	time.Sleep(time.Second)
	semaphore.release()
}

type Semaphore struct {
	ch chan int
}

func NewSemaphore(capacity int) *Semaphore {
	return &Semaphore{ch: make(chan int, capacity)}
}

func (s *Semaphore) acquire() {
	s.ch <- 0
}

func (s *Semaphore) release() {
	<-s.ch
}

type Destination struct {
	passengersToTransfer int
	id                   int
	communication        *Communication
	done                 bool
	doneCh               *chan int
	sync                 sync.Mutex
	group                *sync.WaitGroup
}

func NewDestination(id, passengersToTransfer int, communication *Communication, wg *sync.WaitGroup, done *chan int) *Destination {
	return &Destination{
		passengersToTransfer: passengersToTransfer,
		id:                   id,
		communication:        communication,
		group:                wg,
		doneCh:               done,
	}
}

func (d *Destination) serveAirplane(airplane *Airplane) {
	d.communication.communicateTrap()
	if d.done {
		return
	}
	time.Sleep(200 * time.Millisecond)
	d.addPassengers(airplane)
	d.communication.communicateTerminal()
}

func (d *Destination) addPassengers(airplane *Airplane) {
	d.sync.Lock()
	defer d.sync.Unlock()
	toServe := min(d.passengersToTransfer, airplane.capacity)
	d.passengersToTransfer -= toServe
	if d.passengersToTransfer == 0 {
		d.done = true
		d.group.Done()
	}
	fmt.Printf("Plane %d got passengers in destination %d. Passengers left: %d\n", airplane.id, d.id, d.passengersToTransfer)
}

type PlaneRunnable struct {
	airplane        *Airplane
	destinationList []*Destination
	wg              *sync.WaitGroup
}

func newPlaneRunnable(airplane *Airplane, destinationList []*Destination, latch *sync.WaitGroup) *PlaneRunnable {
	return &PlaneRunnable{airplane, destinationList, latch}
}

func (pr *PlaneRunnable) runAirplane() {
	step := pr.airplane.rangeVal
	currentAirline := 0
	for {
		destination := pr.destinationList[currentAirline]
		select {
		case *destination.doneCh <- 0:
			<-*destination.doneCh
		default:
			return
		}
		destination.serveAirplane(pr.airplane)
		currentAirline = (currentAirline + step) % len(pr.destinationList)
	}
}

func main() {

	var destinationList []*Destination
	wg := sync.WaitGroup{}
	wg.Add(7)
	doneChan := make(chan int, 7)
	for i := 0; i < 7; i++ {
		communication := newCommunication(2, 2)
		destinationList = append(destinationList, NewDestination(i, rand.Intn(800-500+1)+500, communication, &wg, &doneChan))
	}

	for i := 0; i < 4; i++ {
		airplane := newAirplane(i, rand.Intn(100)+100, i+1)
		pr := newPlaneRunnable(airplane, destinationList, &wg)
		go pr.runAirplane()
	}

	wg.Wait()
}
