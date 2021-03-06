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
package gedi.util.program;

import java.util.function.Supplier;
import java.util.logging.Logger;

import gedi.util.userInteraction.progress.Progress;

public class GediProgramContext {

	
	private Logger log;
	private Supplier<Progress> progress;
	private boolean dry;
	
	public GediProgramContext(Logger log, Supplier<Progress> progress, boolean dry) {
		this.log = log;
		this.progress = progress;
		this.dry = dry;
	}

	public Logger getLog() {
		return log;
	}
	
	public Progress getProgress() {
		return progress.get();
	}
	
	public boolean isDryRun() {
		return dry;
	}
}
