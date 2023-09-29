package main

import (
	"fmt"
	"sync"
)

type Customer struct {
	id int
}

type Barbershop struct {
	barbershopQueue chan int
	doneChannel     chan bool
	haircutsChannel chan int
}

func (c Customer) getHaircut(barbershop *Barbershop, group *sync.WaitGroup) {
	barbershop.barbershopQueue <- c.id
	fmt.Printf("%d goes to barber's chair\n", c.id)
	<-barbershop.haircutsChannel
	fmt.Printf("%d gets its haircut and leaves barbershop\n", c.id)
	group.Done()
}

func (b Barbershop) doBarber() {
	id := 0
	for {
		select {
		case id = <-b.barbershopQueue:
			fmt.Printf("customer arrived %d\n", id)
			b.haircutsChannel <- id
		case <-b.doneChannel:
			return
		}
	}
}

func main() {
	barbershop := Barbershop{make(chan int), make(chan bool), make(chan int)}

	customers := make([]Customer, 10)
	for i := 0; i < len(customers); i++ {
		customers[i] = Customer{i + 1}
	}
	group := sync.WaitGroup{}
	group.Add(10)
	go barbershop.doBarber()
	for _, c := range customers {
		go c.getHaircut(&barbershop, &group)
	}
	group.Wait()
	barbershop.doneChannel <- true

}
