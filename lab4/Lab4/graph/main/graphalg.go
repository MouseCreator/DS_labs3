package main

import (
	"fmt"
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

func main() {
	fmt.Println("Hello!")
}
