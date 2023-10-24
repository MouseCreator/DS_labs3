package main

import "sync"

type Barrier struct {
	tasks   int
	current int
	enter   chan int
	leave   chan int
	mutex   sync.Mutex
}

func makeBarrier(tasks int) *Barrier {
	return &Barrier{
		tasks:   tasks,
		current: tasks,
		enter:   make(chan int, tasks),
		leave:   make(chan int, tasks),
		mutex:   sync.Mutex{},
	}
}

func (b *Barrier) await() {
	b.mutex.Lock()
	b.current--
	if b.current == 0 {
		for {
			select {
			case temp := <-b.enter:
				b.leave <- temp
			default:
				b.current = b.tasks
				b.mutex.Unlock()
				return
			}
		}
	} else {
		b.mutex.Unlock()
		b.enter <- 1
		<-b.leave
	}
}
