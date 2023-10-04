package main

import (
	"bufio"
	"fmt"
	"math/rand"
	"os"
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
	fmt.Println("Added node: " + node.id)
}

func (g *Graph) removeNode(id string) {
	for node, edges := range g.nodesMap {
		if node.id == id {
			for _, v := range edges {
				inc := v.to
				edges2 := g.nodesMap[*inc]
				edges2 = remove(edges2, node)
				g.nodesMap[*inc] = edges2
			}
			delete(g.nodesMap, node)
			break
		}
	}
	fmt.Println("Removed node: " + id)
}

func remove(edges []Edge, node Node) []Edge {
	for i, edge := range edges {
		if edge.to.id == node.id {
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

	if len(g.nodesMap) < 2 {
		return
	}
	iter := 0
	var fromNode *Node
	var toNode *Node
	for key := range g.nodesMap {
		if iter == 0 {
			tempFromNode := key
			fromNode = &tempFromNode
			iter++
			continue
		}
		if iter == 1 {
			tempToNode := key
			toNode = &tempToNode
			break
		}
	}
	if g.findEdge(*fromNode, *toNode) != nil {
		return
	}

	g.addEdge(fromNode, toNode, weight)
	fmt.Println("Added edge between " + fromNode.id + " and " + toNode.id + ", weight: " + strconv.Itoa(weight))
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
	if len(randomEdges) == 0 {
		return
	}
	randomIndex := rand.Intn(len(randomEdges))
	edge := randomEdges[randomIndex]
	inc := edge.to

	g.nodesMap[randomNode] = remove(g.nodesMap[randomNode], *inc)
	g.nodesMap[*inc] = remove(g.nodesMap[*inc], randomNode)
	fmt.Println("Removed edge between " + randomNode.id + " and " + inc.id)
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
	if len(randomEdges) == 0 {
		return
	}
	randomIndex := rand.Intn(len(randomEdges))
	edge := randomEdges[randomIndex]
	changeWeight(&edge, weight)
	inc := edge.to
	incEdge := g.findEdge(*inc, randomNode)
	if incEdge == nil {
		g.print()
		panic("No incident edge " + inc.id + " " + randomNode.id)

	}
	changeWeight(incEdge, weight)
	fmt.Println("Changed weight between " + randomNode.id + " and " + inc.id + " to " + strconv.Itoa(weight))
}

func (g *Graph) findEdge(from Node, to Node) *Edge {
	edgesFrom := g.nodesMap[from]
	for _, edge := range edgesFrom {
		if edge.to.id == to.id {
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
		distances[node] = -1
	}
	distances[from] = 0
	for len(visited) < len(g.nodesMap) {
		node := minDistNode(distances, visited)
		if node == nil {
			break
		}
		visited[*node] = true

		for _, edge := range g.nodesMap[*node] {
			if distances[*node]+edge.weight < distances[*edge.to] || distances[*edge.to] == -1 {
				distances[*edge.to] = distances[*node] + edge.weight
			}
		}
		if node.id == to.id {
			fmt.Println("Distance between " + from.id + " and " + to.id + " is " + strconv.Itoa(distances[*node]))
			return
		}
	}
	fmt.Println("No route between " + from.id + " and " + to.id)
}

func minDistNode(distances map[Node]int, visited map[Node]bool) *Node {
	minV := -1
	var minNode *Node
	minNode = nil
	for node, distance := range distances {
		if minV == -1 {
			if !visited[node] && distance != -1 {
				minV = distance
				nodeC := node
				minNode = &nodeC
			}
		} else {
			if !visited[node] && distance != -1 && distance < minV {
				minV = distance
				nodeC := node
				minNode = &nodeC
			}
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

func notDone(done *chan int) bool {
	select {
	case _, ok := <-*done:
		if !ok {
			return false
		}
		return true
	default:
		return true
	}
}

func selectId(present []bool) int {
	for i, v := range present {
		if v == false {
			present[i] = true
			return i
		}
	}
	panic("Cannot generate id for node!")
}

func selectExistingId(present []bool) int {
	for i, v := range present {
		if v == true {
			present[i] = false
			return i
		}
	}
	panic("Cannot get node to remove!")
}

func (g *Graph) rtPriceChange(done *chan int, group *sync.WaitGroup) {
	for notDone(done) {
		weight := 1 + rand.Intn(100)
		writeLocked(g, g.changeRandomWeight, weight)
		time.Sleep(150 * time.Millisecond)
	}
	fmt.Println("Done Price")
	group.Done()
}

func (g *Graph) rtEdgesChange(done *chan int, group *sync.WaitGroup) {
	for notDone(done) {
		action := rand.Intn(10)
		if action != 0 {
			weight := 1 + rand.Intn(100)
			writeLocked(g, g.addRandomEdge, weight)
		} else {
			writeLocked(g, g.removeRandomEdge)
		}
		time.Sleep(250 * time.Millisecond)

	}
	fmt.Println("Done Edges")
	group.Done()
}

func (g *Graph) rtNodesChange(done *chan int, group *sync.WaitGroup) {
	const NodeLimit = 100
	currentNodes := 0
	present := make([]bool, NodeLimit)
	for notDone(done) {
		action := rand.Intn(2)
		if (action == 0 || currentNodes < 10) && currentNodes < NodeLimit {
			id := selectId(present)
			currentNodes++
			writeLocked(g, g.addNode, strconv.Itoa(id))
			continue
		}
		if action == 1 && currentNodes > 0 {
			currentNodes--
			id := selectExistingId(present)
			writeLocked(g, g.removeNode, strconv.Itoa(id))
		}
		time.Sleep(400 * time.Millisecond)

	}
	fmt.Println("Done Nodes")
	group.Done()
}

func (g *Graph) rtPathFinder(done *chan int, group *sync.WaitGroup) {

	for notDone(done) {
		readLocked(g, g.findRandomPath)
		time.Sleep(100 * time.Millisecond)
	}
	fmt.Println("Done Finder")
	group.Done()
}

func main() {
	graph := Graph{sync.RWMutex{}, make(map[Node][]Edge)}
	group := sync.WaitGroup{}
	group.Add(4)
	done := make(chan int)

	go graph.rtNodesChange(&done, &group)
	go graph.rtEdgesChange(&done, &group)
	go graph.rtPriceChange(&done, &group)
	go graph.rtPathFinder(&done, &group)

	reader := bufio.NewReader(os.Stdin)

	_, err := reader.ReadString('\n')

	if err != nil {
		fmt.Println("Error reading input:", err)
		return
	}

	close(done)
	group.Wait()

}
