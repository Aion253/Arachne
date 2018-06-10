package net.aionstudios.aosarachne.service;

import net.aionstudios.api.util.DatabaseUtils;

public class PageStorageService {
	
	private static String createPageTable = "CREATE TABLE `arachnepages` (\r\n" + 
			"	`url` VARCHAR(2047) NOT NULL COLLATE 'latin1_general_cs',\r\n" + 
			"	`domain` VARCHAR(255) NOT NULL COLLATE 'latin1_general_cs',\r\n" + 
			"	`secureConnection` TINYINT(1) NOT NULL,\r\n" + 
			"	`title` VARCHAR(255) NULL DEFAULT NULL COLLATE 'latin1_general_cs',\r\n" + 
			"	`desc` VARCHAR(511) NULL DEFAULT NULL COLLATE 'latin1_general_cs',\r\n" + 
			"	`contentType` VARCHAR(255) NOT NULL COLLATE 'latin1_general_cs',\r\n" + 
			"	`pageRawText` MEDIUMTEXT NULL COLLATE 'latin1_general_cs',\r\n" + 
			"	`outgoingLinkID` VARCHAR(15) NOT NULL COLLATE 'latin1_general_cs',\r\n" + 
			"	`internalLinkID` VARCHAR(15) NOT NULL COLLATE 'latin1_general_cs',\r\n" + 
			"	`pictureLinkID` VARCHAR(15) NOT NULL COLLATE 'latin1_general_cs',\r\n" + 
			"	`keywordLinkID` VARCHAR(15) NOT NULL COLLATE 'latin1_general_cs',\r\n" + 
			"	`faviconUrl` VARCHAR(2047) NULL DEFAULT NULL COLLATE 'latin1_general_cs',\r\n" + 
			"	`usesJavascript` TINYINT(1) NOT NULL,\r\n" + 
			"	`usesHtmlFive` TINYINT(1) NOT NULL,\r\n" + 
			"	`usesViewportMeta` TINYINT(1) NOT NULL,\r\n" + 
			"	`usesTwitterExtras` TINYINT(1) NOT NULL,\r\n" + 
			"	`usesOpengraphExtras` TINYINT(1) NOT NULL,\r\n" + 
			"	`usesAppleExtras` TINYINT(1) NOT NULL,\r\n" + 
			"	`usesMicrosoftExtras` TINYINT(1) NOT NULL,\r\n" + 
			"	PRIMARY KEY (`url`)\r\n" + 
			")\r\n" + 
			"COLLATE='latin1_general_cs'\r\n" + 
			"ENGINE=InnoDB\r\n" + 
			";\r\n" + 
			"";
	
	private static String createLinksTable = "CREATE TABLE `arachnelinks` (\r\n" + 
			"	`linkID` VARCHAR(15) NOT NULL COMMENT 'The first character recognizes link type (internal, external, picture or keyword) and the remaining 14 are lowercase alphanumeric for 36^14 unique options, 6 sextillion greatly exceed the 4.54 billion webpages prediction.' COLLATE 'latin1_general_cs',\r\n" + 
			"	`links` MEDIUMTEXT NULL COMMENT 'Links should not exceed 2048 locations on a maximum length of 2048 characters in a  string including a blank space delimiter. Max length should be treated as 4194303 although not set explicitly.' COLLATE 'latin1_general_cs',\r\n" + 
			"	PRIMARY KEY (`linkID`)\r\n" + 
			")\r\n" + 
			"COLLATE='latin1_general_cs'\r\n" + 
			"ENGINE=InnoDB\r\n" + 
			";";
	
	public static boolean createTables() {
		DatabaseUtils.prepareAndExecute(createPageTable, false);
		DatabaseUtils.prepareAndExecute(createLinksTable, false);
		return true;
	}

}
