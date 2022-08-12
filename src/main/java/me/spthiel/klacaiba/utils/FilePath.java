package me.spthiel.klacaiba.utils;

import com.mumfrey.liteloader.core.LiteLoader;

import javax.annotation.Nonnull;
import java.io.File;

public class FilePath {

	@Nonnull
	public static File getFile(@Nonnull String path) {
		if(path.startsWith("~")) {
			return new File(LiteLoader.getGameDirectory(),path.substring(1));
		}
		return new File(path);
	}

}
