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
package gedi.util.io.text;


public class StringLineWriter implements LineWriter, CharSequence {

	
	private StringBuilder sb = new StringBuilder();

	
	@Override
	public void write(String line) {
		sb.append(line);
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() {
	}

	
	@Override
	public String toString() {
		return sb.toString();
	}

	public void setContent(String content) {
		sb.delete(0, sb.length());
		sb.append(content);
	}

	@Override
	public int length() {
		return sb.length();
	}

	@Override
	public char charAt(int index) {
		return sb.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return sb.subSequence(start, end);
	}
	
}
