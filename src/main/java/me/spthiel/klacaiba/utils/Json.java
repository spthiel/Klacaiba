package me.spthiel.klacaiba.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Json
{
  private ArrayList<String> keys = new ArrayList<String>();
  
  public Json(String s)
  {
    String[] all = getBracketContent(s, '[', ']').split("~");
    for (String currentAll : all) {
      String[] attribs = getBracketContent(currentAll, '{', '}').split("~");
      for (String currAtt : attribs) {
        String[] kommas = currAtt.split(",");
        for (String currKomma : kommas) {
          String[] doppel = currKomma.split(":");
          if (doppel.length < 2) {
            break;
          }
          this.keys.add(doppel[0]);
          this.keys.add(doppel[1]);
        }
      }
    }
  }
  
  public String get(String key)
  {
    int idx = this.keys.indexOf("\"" + key + "\"");
    if (idx == -1) {
      return "";
    }
    return (String)this.keys.get(idx + 1);
  }
  
  public String[] getAll(String key)
  {
    String out = "";
    for (int i = 0; i < this.keys.size() - 1; i++) {
      if (((String)this.keys.get(i)).equalsIgnoreCase("\"" + key + "\"")) {
        out = out + (String)this.keys.get(i + 1) + "~";
      }
    }
    return out.split("~");
  }
  
  public String getBracketContent(String string, char openBracket, char closedBracket)
  {
    String result = "";
    Matcher m = Pattern.compile("\\" + openBracket + "(.+?)\\" + closedBracket).matcher(string);
    while (m.find()) {
      result = result + m.group(1) + "~";
    }
    return result;
  }
}
