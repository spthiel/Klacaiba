package me.spthiel.klacaiba.module.actions.information;


import net.eq2online.macros.scripting.api.*;
import me.spthiel.klacaiba.base.BaseScriptAction;

import javax.annotation.Nonnull;
import java.security.MessageDigest;

public class Hash extends BaseScriptAction {
    public Hash() {
        super("hash");
    }


    @Nonnull
    @Override
    public String getUsage() {
        return "hash(<input>)";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Returns the input hashed into sha-256";
    }

    @Nonnull
    @Override
    public String getReturnType() {
        return "Hash of input or -1 if input is not supplied";
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length == 0) {
            return new ReturnValue("-1");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(provider.expand(macro,params[0],false).getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return new ReturnValue(hexString.toString());
        } catch(Exception e) {
            return null;
        }
    }
}
