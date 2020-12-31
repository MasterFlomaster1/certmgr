/*
 * Copyright (c) 2015-2021 Holger de Carne and contributors, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.carne.certmgr.jfx;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import de.carne.boot.ShutdownHooks;
import de.carne.boot.logging.Log;
import de.carne.boot.logging.LogLevel;
import de.carne.boot.logging.Logs;
import de.carne.certmgr.jfx.resources.Images;
import de.carne.certmgr.jfx.store.StoreController;
import de.carne.jfx.stage.StageController;
import de.carne.jfx.stage.logview.LogViewImages;
import de.carne.util.cmdline.CmdLineException;
import de.carne.util.cmdline.CmdLineProcessor;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX application class responsible for running the UI.
 */
public class CertMgrApplication extends Application {

	private static final Log LOG = new Log();

	/**
	 * Launch this JavaFX application.
	 *
	 * @param args The application's command line.
	 */
	public static void launch(String[] args) {
		Application.launch(CertMgrApplication.class, args);
	}

	@Override
	public void start(@Nullable Stage primaryStage) throws Exception {
		// Evaluate command line as soon as possible to apply logging options as
		// soon as possible
		File defaultStoreHome = evalCmdLine();

		LOG.info("JavaFX GUI starting...");

		LogViewImages.LEVEL_IMAGES.registerImage(LogLevel.LEVEL_TRACE, Images.TRACE16);
		LogViewImages.LEVEL_IMAGES.registerImage(LogLevel.LEVEL_DEBUG, Images.DEBUG16);
		LogViewImages.LEVEL_IMAGES.registerImage(LogLevel.LEVEL_INFO, Images.INFO16);
		LogViewImages.LEVEL_IMAGES.registerImage(LogLevel.LEVEL_WARNING, Images.WARNING16);
		LogViewImages.LEVEL_IMAGES.registerImage(LogLevel.LEVEL_ERROR, Images.ERROR16);
		LogViewImages.LEVEL_IMAGES.registerImage(LogLevel.LEVEL_NOTICE, Images.NOTICE16);

		StoreController store = StageController.loadPrimaryStage(Objects.requireNonNull(primaryStage),
				StoreController.class);

		store.show();
		if (defaultStoreHome != null) {
			store.openStore(defaultStoreHome);
		}
	}

	@Override
	public void stop() throws Exception {
		LOG.info("JavaFX GUI stopped");
		ShutdownHooks.trigger();
	}

	@Nullable
	private File evalCmdLine() {
		CmdLineProcessor cmdLine = new CmdLineProcessor("certmgr", getParameters().getRaw());
		List<String> defaultArgs = new ArrayList<>();

		cmdLine.onSwitch((s) -> applyLogConfig(Logs.CONFIG_VERBOSE)).arg("--verbose");
		cmdLine.onSwitch((s) -> applyLogConfig(Logs.CONFIG_DEBUG)).arg("--debug");
		cmdLine.onUnnamedOption((s) -> defaultArgs.add(s));
		try {
			cmdLine.process();
			LOG.info("Running command line ''{0}''", cmdLine);
		} catch (CmdLineException e) {
			LOG.warning(e, "Invalid command line ''{0}''; ", cmdLine);
		}

		File defaultStoreHome = null;

		for (String defaultArg : defaultArgs) {
			if (defaultStoreHome == null) {
				defaultStoreHome = new File(defaultArg);
			} else {
				LOG.warning("Ignoring extra store home argument ''{0}''", defaultStoreHome);
			}
		}
		return defaultStoreHome;
	}

	private void applyLogConfig(String config) {
		try {
			Logs.readConfig(config);
		} catch (IOException e) {
			LOG.warning(e, "Failed to apply log configuraiton ''{0}''", config);
		}
	}

}
