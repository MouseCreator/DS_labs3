package main

import (
	"fmt"
	"math"
	"math/rand"
	"reflect"
	"strconv"
	"sync"
	"time"
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

func remove(edges []Edge, node Node) []Edge {
	for i, edge := range edges {
		if edge.to == &node {
			return removeEdgeIndex(edges, i)
		}
	}
	return edges
}

func removeEdgeIndex(s []Edge, index int) []Edge {
	ret := make([]Edge, 0)
	ret = append(ret, s[:index]...)
	return append(ret, s[index+1:]...)
}

func (g *Graph) addEdge(fromNode *Node, toNode *Node, weight int) {
	var fromEdges = g.nodesMap[*fromNode]
	var toEdges = g.nodesMap[*toNode]

	fromEdges = append(fromEdges, Edge{toNode, weight})
	toEdges = append(toEdges, Edge{fromNode, weight})
	g.nodesMap[*fromNode] = fromEdges
	g.nodesMap[*toNode] = toEdges

}

func (g *Graph) addRandomEdge(weight int) {
	var fromNode *Node
	var toNode *Node

	if len(g.nodesMap) < 2 {
		return
	}
	iter := 0
	for key := range g.nodesMap {
		if iter == 0 {
			iter++
			fromNode = &key
			continue
		}
		if iter == 1 {
			toNode = &key
			break
		}
	}
	g.addEdge(fromNode, toNode, weight)

}

func (g *Graph) removeRandomEdge() {
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
	inc := edge.to

	remove(g.nodesMap[randomNode], *inc)
	remove(g.nodesMap[*inc], randomNode)

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
		panic("No incident edge")
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

func (g *Graph) dijkstra(from Node, to Node) {
	visited := make(map[Node]bool)
	distances := make(map[Node]int)
	for node := range g.nodesMap {
		distances[node] = math.MaxInt32
	}
	distances[from] = 0
	for len(visited) < len(g.nodesMap) {
		node := minDistNode(distances, visited)
		if node == nil {
			break
		}
		visited[*node] = true

		for _, edge := range g.nodesMap[*node] {
			if distances[*node]+edge.weight < distances[*edge.to] {
				distances[*edge.to] = distances[*node] + edge.weight
			}
		}
		if *node == to {
			fmt.Println("Distance between " + from.id + " and " + to.id + " is " + strconv.Itoa(distances[*node]))
			return
		}
	}
	fmt.Println("No route between " + from.id + " and " + to.id)
}

func minDistNode(distances map[Node]int, visited map[Node]bool) *Node {
	minV := math.MaxInt32
	var minNode *Node
	for node, distance := range distances {
		if !visited[node] && distance < minV {
			minV = distance
			minNode = &node
		}
	}
	return minNode
}

func (g *Graph) findRandomPath() {
	var iter int
	var findFrom Node
	var findTo Node
	if len(g.nodesMap) < 2 {
		return
	}
	for key := range g.nodesMap {
		if iter == 0 {
			iter++
			findFrom = key
			continue
		}
		if iter == 1 {
			findTo = key
			break
		}
	}
	g.dijkstra(findFrom, findTo)
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

func (g *Graph) rtPriceChange(done chan int) {
	for notDone(done) {
		weight := 1 + rand.Intn(100)
		writeLocked(g, g.changeRandomWeight, weight)
		time.Sleep(100 * time.Millisecond)
	}
}

func (g *Graph) rtEdgesChange(done chan int) {
	for notDone(done) {
		weight := 1 + rand.Intn(100)
		writeLocked(g, g.changeRandomWeight, weight)
		time.Sleep(100 * time.Millisecond)
	}
}

func main() {
	//graph := Graph{sync.RWMutex{}, make(map[Node][]Edge)}

	fmt.Println("Hello!")
}
