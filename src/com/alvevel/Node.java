package com.alvevel;

public class Node {
    private Node left;
    private Node right;
    private int value;
    private int weight;

    public Node() {}

    public Node(int value, int weight) {
        this.value = value;
        this.weight = weight;
    }

    public Node(int weight) {
        this.weight = weight;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public  int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Node{" +
                "left=" + left +
                ", right=" + right +
                ", value=" + value +
                ", weight=" + weight +
                '}';
    }
}
