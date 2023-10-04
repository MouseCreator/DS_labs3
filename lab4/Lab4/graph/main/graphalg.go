package main

import (
	"fmt"
	"math/rand"
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

func changeWeight(edge *Edge, newWeight int) {
	edge.weight = newWeight
}

func main() {
	fmt.Println("Hello!")
}
