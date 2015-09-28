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

		byte[] x = new byte[1];

		for (int i = 0; i < charNum; i++) {

			x[0] = 0;
			if (curNode.next[i] == null || curNode.next[i].count == 0) {

				out.write(x);
				out.write(x);
			}
			else {

				x[0] = 1;
				out.write(x);
				x[0] = (byte) (curNode.next[i].isTerm ? 1 : 0);
				out.write(x);
				makeOutput(curNode.next[i], out);

			}

		}
	}

	public void serialize(OutputStream out) {

		try {

			byte[] t = new byte[1];
			t[0] = (byte) (root.isTerm ? 1 : 0);
			out.write(t);

			makeOutput(root, out);
		}
		catch (IOException io) {
			throw new SerializationException();
		}
	}


	private void makeTree(Node curNode, InputStream in) throws IOException {

		byte[] x = new byte[1];
		byte[] y = new byte[1];
		int num;
		for (int i = 0; i < charNum; i++) {

			int k = in.read(x);
			int l = in.read(y);

			if (x[0] == 1) {

				curNode.next[i] = new Node();
				curNode.next[i].isTerm = (y[0] == 1);
				curNode.count += (int)y[0];
				makeTree(curNode.next[i], in);
				curNode.count += curNode.next[i].count;
			}

		}

	}

	public void deserialize(InputStream in) {

		root = new Node();
		size = 0;

		try {

			byte[] x = new byte[1];
			int k = in.read(x);
			root.isTerm = (x[0] == 1);
			root.count += (int)x[0];
			makeTree(root, in);
		}
		catch (IOException io) {
			throw new SerializationException();
		}

		size = root.count;
	}

	public static void main(String[] args) {

         StringSetImpl Bor = new StringSetImpl();

         Bor.add("abc");
         Bor.add("cde");
         Bor.add("");
         Bor.add("");
         Bor.add("cde");
         Bor.remove("cde");
         Bor.add("cde");
         Bor.add("aa");
         Bor.remove("aa");
         Bor.remove("");
         Bor.add("");
         //Bor.remove("");

         System.out.println(Bor.size());
         System.out.println(Bor.howManyStartsWithPrefix(""));
         System.out.println(Bor.howManyStartsWithPrefix("cde"));
     }
}