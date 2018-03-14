/**
 * 
 *    Copyright 2017 Florian Erhard
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 */
package gedi.util.r;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class RProcess {

	private Thread thread;
	private Process process;
	
	public RProcess() throws IOException {
		
		ProcessBuilder b = new ProcessBuilder();
		b.redirectOutput(Redirect.INHERIT);
		b.redirectError(Redirect.INHERIT);
		
		b.command("R", "--interactive","--no-save", "--no-restore");
		process = b.start();
		
		thread = new Thread(()->{
			try {
				process.waitFor();
			} catch (Exception e) {
			} 
		});
		thread.setDaemon(true);
		thread.setName("R-Process");
		thread.start();
	}
	
	public OutputStream getStdin() {
		return process.getOutputStream();
	}
	
	public void read() throws IOException {
		read(System.in);
	}
	
	public void read(InputStream is) throws IOException {
		int key;
		InputStreamReader in = new InputStreamReader(is);
		while (process.isAlive()) {
			key = in.read();
			if (key==10)
				return;
			if (process.isAlive()) {
				getStdin().write(key);
				getStdin().flush();
			}
		}
	}
	
	public static final int port = 12844;
	public static final String LISTEN_CMD = ".Internal(loadFromConn2(socketConnection(port="+port+",blocking=T,server=T,open=\"r+b\"),environment(),F))\n";
	public boolean isRunning() {
		return process.isAlive();
	}

	public RDataWriter startSetting() throws UnknownHostException, IOException {
		new PrintWriter(getStdin()).append(LISTEN_CMD).flush();
		Socket socket = null;
		while (socket==null) {
			try {
				socket = new Socket("localhost",port);
			} catch (ConnectException e) {
			}
		}
		RDataWriter re = new RDataWriter(socket.getOutputStream());
		re.writeHeader();
		return re;
	}
	
	
	
}
