package services.configService;

import java.util.EnumMap;

public enum Language {
	EN_UK;
	
	//
	static EnumMap<Language,String> map = new EnumMap<>(Language.class);
	static {
        for (Language language : values())
            map.put(language, language.name());
    }
}
