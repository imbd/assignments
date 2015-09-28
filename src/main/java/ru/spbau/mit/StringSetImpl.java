package ru.spbau.mit;


import java.io.*;



public class StringSetImpl implements StreamSerializable,StringSet {


	private static class Node {

		private int count = 0;
		private Node[] next = new Node[charNum];
		private boolean isTerm = false;

	}

	private int size = 0;
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

		Node curNode = root;
		if (contains(element)) {
			return false;
		}
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
		size++;
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

		size--;
		curNode.isTerm = false;
		return true;
	}


	public int size() {
		return size;
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

		byte[] b = new byte[4];
		int a;
		for (int i = 0; i < charNum; i++) {

			if (curNode.next[i] == null || curNode.next[i].count == 0) {

				a = -1;
				b[3] = (byte) a;
				b[2] = (byte) ((a >> 8));
				b[1] = (byte) ((a >> 16));
				b[0] = (byte) ((a >> 24));
				out.write(b);
			}
			else {

				a = curNode.next[i].count;
				b[3] = (byte) a;
				b[2] = (byte) ((a >> 8));
				b[1] = (byte) ((a >> 16));
				b[0] = (byte) ((a >> 24));
				out.write(b);
				byte[] x = new byte[1];
				x[0] = (byte) (curNode.next[i].isTerm ? 1 : 0);
				out.write(x);
				makeOutput(curNode.next[i], out);

			}

		}
	}

	public void serialize(OutputStream out) {

		try {
			byte[] b = new byte[4];
			int a = root.count;
			b[3] = (byte) a;
			b[2] = (byte) ((a >> 8));
			b[1] = (byte) ((a >> 16));
			b[0] = (byte) ((a >> 24));
			out.write(b);

			byte[] x = new byte[1];
			x[0] = (byte) (root.isTerm ? 1 : 0);
			out.write(x);

			makeOutput(root, out);
		}
		catch (IOException io) {
			throw new SerializationException();
		}
	}


	private void makeTree(Node curNode, InputStream in) throws IOException {

		byte[] data = new byte[4];
		byte[] x = new byte[1];
		int num;
		for (int i = 0; i < charNum; i++) {

			int k = in.read(data);
			num = (data[0] << 24) | (data[1] & 0xFF) << 16 | (data[2] & 0xFF) << 8 | (data[3] & 0xFF);

			if (num != -1) {

				int l = in.read(x);
				curNode.next[i] = new Node();
				curNode.next[i].isTerm = (x[0] == 1);
				curNode.next[i].count = num;
				makeTree(curNode.next[i], in);
			}

		}

	}

	public void deserialize(InputStream in) {

		root = new Node();
		size = 0;

		try {

			byte[] data = new byte[4];
			byte[] x = new byte[1];
			int k = in.read(data);
			int num = (data[0] << 24) | (data[1] & 0xFF) << 16 | (data[2] & 0xFF) << 8 | (data[3] & 0xFF);

			if (num != -1) {
				int l = in.read(x);
				root.isTerm = (x[0] == 1);
				root.count = num;
			}

			makeTree(root, in);
		}
		catch (IOException io) {
			throw new SerializationException();
		}

		size = root.count;
	}
}