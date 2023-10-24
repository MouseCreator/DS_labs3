package main

import (
	"fmt"
	"sync"
	"time"
)

type Parking struct {
	freeSpaces chan int
}

type Car struct {
	id int
}

func createParking(spaces int) Parking {
	parking := Parking{make(chan int, spaces)}
	for i := 0; i < spaces; i++ {
		parking.freeSpaces <- 0
	}
	return parking
}

func (c *Car) tryAcquire(p *Parking, acquireFor int, group *sync.WaitGroup) {
	select {
	case <-p.freeSpaces:
		fmt.Printf("Car %d acquired space\n", c.id)
		time.Sleep(time.Duration(acquireFor) * time.Second)
		p.freeSpaces <- 0
		fmt.Printf("Car %d left\n", c.id)
	case <-time.After(3 * time.Second):
		fmt.Printf("Timeout occurred. Car %d didn't acquire space\n", c.id)
	}
	group.Done()
}

func main() {
	carsNum := 20
	wg := sync.WaitGroup{}
	wg.Add(carsNum)
	parking := createParking(5)
	for i := 0; i < carsNum; i++ {
		car := Car{i + 1}
		go car.tryAcquire(&parking, 9, &wg)
		time.Sleep(time.Second)
	}
	wg.Wait()
}
