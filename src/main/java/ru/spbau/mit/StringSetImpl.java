package ru.spbau.mit;


import java.io.*;



public class StringSetImpl implements StreamSerializable,StringSet {


	private static class Node {

		private int count = 0;
		private Node[] next = new Node[charNum];
		private boolean isTerm = false;

	}

	private Node root = new Node();
	private static final int charNum = 52;


	private int number(char c) {
		if (Character.isUpperCase(c)) {
			return (c - 'A');
		}
		else {
			return (c - 'a' + 26);
		}
	}

	private boolean exists(String element) {
		Node curNode = root;
		for (char c: element.toCharArray()) {
			int num = number(c);
			if (curNode.next[num] == null) {
				return false;
			}
			curNode = curNode.next[num];
		}
		return true;
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

		if (!exists(element)) {
			return false;
		}
		Node curNode = root;
		for (char c: element.toCharArray()) {
			int num = number(c);
			curNode = curNode.next[num];
		}
		return curNode.isTerm;
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

		if (!exists(prefix)) {
			return 0;
		}
		Node curNode = root;
		for (char c: prefix.toCharArray()) {
			int num = number(c);
			curNode = curNode.next[num];
		}
		return curNode.count;
	}


	private void makeOutput(Node curNode, OutputStream out) throws IOException {

		byte x;

		for (int i = 0; i < charNum; i++) {

			x = 0;
			if (curNode.next[i] == null || curNode.next[i].count == 0) {

				out.write(x);
				out.write(x);
			}
			else {

				x = 1;
				out.write(x);
				x = (byte) (curNode.next[i].isTerm ? 1 : 0);
				out.write(x);
				makeOutput(curNode.next[i], out);

			}
		}
	}

	public void serialize(OutputStream out) {

		try {


			byte t = (byte) (root.isTerm ? 1 : 0);
			out.write(t);
			makeOutput(root, out);
		}
		catch (IOException io) {
			throw new SerializationException();
		}
	}


	private void makeTree(Node curNode, InputStream in) throws IOException {

		int num;
		for (int i = 0; i < charNum; i++) {

			byte k = (byte)in.read();
			byte l = (byte)in.read();

			if (k == 1) {

				curNode.next[i] = new Node();
				curNode.next[i].isTerm = (l == 1);
				curNode.next[i].count += (int)l;
				makeTree(curNode.next[i], in);
				curNode.count += curNode.next[i].count;
			}

		}
	}

	public void deserialize(InputStream in) {

		root = new Node();
		try {

			byte x = (byte)in.read();
			root.isTerm = (x == 1);
			root.count += (int)x;
			makeTree(root, in);
		}
		catch (IOException io) {
			throw new SerializationException();
		}
	}
}