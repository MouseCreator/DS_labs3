package main

import (
	"fmt"
	"sync"
)

type Graph struct {
	mutex sync.RWMutex
}

func main() {
	fmt.Println("Hello!")
}
