package ru.spbau.mit;


import java.io.*;
import java.util.BitSet;


public class StringSetImpl implements StreamSerializable,StringSet {


	private static class Node {

		private int count = 0;
		private Node[] next = new Node[ALPHABET_SIZE];
		private boolean isTerm = false;

	}

	private Node root = new Node();

	private static final int ALPHABET_SIZE = 52;


	private int number(char c) {

		if (Character.isUpperCase(c)) {
			return (c - 'A');
		}
		else {
			return (c - 'a' + 26);
		}

	}

	private boolean exists(String element) {

		return getNode(element) != null;

	}

	private Node getNode(String element) {

		Node curNode = root;
		for (char c: element.toCharArray()) {
			int num = number(c);
			if (curNode.next[num] == null) {
				return null;
			}
			curNode = curNode.next[num];
		}
		return curNode;

	}

	public boolean add(String element) {

		if (contains(element)) {
			return false;
		}
		Node curNode = root;
		root.count++;
		for (char c: element.toCharArray()) {
			int num = number(c);
			if (curNode.next[num] == null) {
				curNode.next[num] = new Node();
			}
			curNode.next[num].count++;
			curNode = curNode.next[num];
		}
		curNode.isTerm = true;
		return true;

	}


	public boolean contains(String element) {

		Node node = getNode(element);
		return (node != null && node.isTerm);

	}


	public boolean remove(String element) {

		Node curNode = root;
		if (!contains(element)) {
			return false;
		}
		root.count--;
		for (char c: element.toCharArray()) {
			int num = number(c);
			curNode.next[num].count--;
			curNode = curNode.next[num];
		}
		curNode.isTerm = false;
		return true;

	}

	public int size() {

		return root.count;

	}

	public int howManyStartsWithPrefix(String prefix) {

		Node fNode  = getNode(prefix);
		return (fNode == null ? 0 : fNode.count);

	}


	private void makeOutput(Node curNode, OutputStream out) throws IOException {

		BitSet myBitSet  = new BitSet(ALPHABET_SIZE + 2);
		myBitSet.set(ALPHABET_SIZE, curNode.isTerm);
		myBitSet.set(ALPHABET_SIZE + 1, true);
		for (int i = 0; i < ALPHABET_SIZE; i++) {
			myBitSet.set(i, curNode.next[i] != null);
		}
		out.write(myBitSet.toByteArray());
		for (int i = 0; i < ALPHABET_SIZE; i++) {
			if (curNode.next[i] != null) {
				makeOutput(curNode.next[i], out);
			}
		}
	}

	public void serialize(OutputStream out) {

		try {
			makeOutput(root, out);
		}
		catch (IOException io) {
			throw new SerializationException();
		}

	}

	private void makeTree(Node curNode, InputStream in) throws IOException {

		byte[] b = new byte[ALPHABET_SIZE / 8 + 1];
		int l = in.read(b);
		BitSet myBitSet = BitSet.valueOf(b);
		curNode.isTerm = myBitSet.get(ALPHABET_SIZE);
		curNode.count += (curNode.isTerm ? 1 : 0);
		for (int i = 0; i < ALPHABET_SIZE; i++) {
			if (myBitSet.get(i)) {
				curNode.next[i] = new Node();
				makeTree(curNode.next[i], in);
				curNode.count += curNode.next[i].count;
			}
		}

	}

	public void deserialize(InputStream in) {

		root = new Node();
		try {
			makeTree(root, in);
		}
		catch (IOException io) {
			throw new SerializationException();
		}

	}

}