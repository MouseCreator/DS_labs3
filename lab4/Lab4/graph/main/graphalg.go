package main

import (
	"fmt"
	"math/rand"
	"reflect"
	"strconv"
	"sync"
)

type Node struct {
	id string
}

type Edge struct {
	to     *Node
	weight int
}

type Graph struct {
	mutex    sync.RWMutex
	nodesMap map[Node][]Edge
}

func (g *Graph) addNode(id string) {
	node := Node{id}
	edges := make([]Edge, 0)
	g.nodesMap[node] = edges
}

func (g *Graph) removeNode(id string) {
	for node, edges := range g.nodesMap {
		if node.id == id {
			for _, v := range edges {
				inc := v.to
				edges2 := g.nodesMap[*inc]
				edges2 = remove(edges2, node)
				g.nodesMap[*inc] = edges
			}
			delete(g.nodesMap, node)
			return
		}
	}
}

func removeEdgeIndex(s []Edge, index int) []Edge {
	ret := make([]Edge, 0)
	ret = append(ret, s[:index]...)
	return append(ret, s[index+1:]...)
}

func remove(edges []Edge, node Node) []Edge {
	for i, edge := range edges {
		if edge.to == &node {
			return removeEdgeIndex(edges, i)
		}
	}
	return edges
}

func (g *Graph) addEdge(from string, to string, weight int) {
	var fromNode *Node
	var fromEdges *[]Edge
	var toNode *Node
	var toEdges *[]Edge
	for node, edges := range g.nodesMap {
		if node.id == from {
			fromNode = &node
			fromEdges = &edges
		}
		if node.id == to {
			toNode = &node
			toEdges = &edges
		}
	}

	*fromEdges = append(*fromEdges, Edge{toNode, weight})
	*toEdges = append(*toEdges, Edge{fromNode, weight})
	g.nodesMap[*fromNode] = *fromEdges
	g.nodesMap[*toNode] = *toEdges

}

func (g *Graph) print() {
	str := ""
	for node, edges := range g.nodesMap {
		str = str + node.id + " => "
		for _, edge := range edges {
			weight := strconv.Itoa(edge.weight)
			str = str + edge.to.id + " (" + weight + "), "
		}
		str += "\n"
	}
	fmt.Println(str)

}

func (g *Graph) changeRandomWeight(weight int) {
	var randomNode Node
	var randomEdges []Edge

	if len(g.nodesMap) == 0 {
		return
	}
	for key, edges := range g.nodesMap {
		randomNode = key
		randomEdges = edges
		break
	}
	randomIndex := rand.Intn(len(randomEdges))
	edge := randomEdges[randomIndex]
	changeWeight(&edge, weight)
	inc := edge.to
	incEdge := g.findEdge(*inc, randomNode)
	if incEdge == nil {
		fmt.Println("ERROR: NO INCIDENT EDGE")
	}
	changeWeight(incEdge, weight)
}

func (g *Graph) findEdge(from Node, to Node) *Edge {
	edgesFrom := g.nodesMap[from]
	for _, edge := range edgesFrom {
		if edge.to == &to {
			return &edge
		}
	}
	return nil
}

func readLocked(g *Graph, fn interface{}, params ...interface{}) {
	g.mutex.RLock()
	defer g.mutex.RUnlock()

	f := reflect.ValueOf(fn)
	if len(params) != f.Type().NumIn() {
		panic("Incorrect number of parameters for function")
	}

	inputs := make([]reflect.Value, len(params))
	for i, param := range params {
		inputs[i] = reflect.ValueOf(param)
	}
	f.Call(inputs)
}

func writeLocked(g *Graph, fn interface{}, params ...interface{}) {
	g.mutex.Lock()
	defer g.mutex.Unlock()

	f := reflect.ValueOf(fn)
	if len(params) != f.Type().NumIn() {
		panic("Incorrect number of parameters for function")
	}

	inputs := make([]reflect.Value, len(params))
	for i, param := range params {
		inputs[i] = reflect.ValueOf(param)
	}
	f.Call(inputs)
}

func changeWeight(edge *Edge, newWeight int) {
	edge.weight = newWeight
}

func notDone(done chan int) bool {
	select {
	case d := <-done:
		done <- d
		return false
	default:
		return true
	}
}

func rtPriceChange(done chan int) {
	for notDone(done) {

	}
}

func main() {
	//graph := Graph{sync.RWMutex{}, make(map[Node][]Edge)}

	fmt.Println("Hello!")
}
