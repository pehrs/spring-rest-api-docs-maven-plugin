package com.pehrs.spring.api.doc.mvn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * A Thread that reads an input stream and prints it to a PrintStream
 * 
 * @author matti
 */
public class ThreadedStreamReader extends Thread {

	private final InputStream in;
	private final PrintWriter out;
	private final String prefix;

	public ThreadedStreamReader(InputStream input, PrintWriter output) {
		this.prefix = "";
		this.in = input;
		this.out = output;
	}

	public void run() {
		BufferedReader buffer = null;
		try {
			InputStreamReader isr = new InputStreamReader(in);
			buffer = new BufferedReader(isr);
			String line = null;
			while ((line = buffer.readLine()) != null) {
				out.println(prefix + line);
			}
			out.flush();
		} catch (IOException e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			out.flush();
			if (buffer != null) {
				try {
					buffer.close();
				} catch (IOException e) {
					// e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}
}
