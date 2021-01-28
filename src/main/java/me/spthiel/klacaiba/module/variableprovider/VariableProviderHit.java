package me.spthiel.klacaiba.module.variableprovider;

import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.variable.VariableCache;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import me.spthiel.klacaiba.ModuleInfo;

@APIVersion(ModuleInfo.API_VERSION)
public class VariableProviderHit extends VariableCache {
	
	@Override
	public void updateVariables(boolean clock) {
		
		if (!clock) {
			return;
		}
		
		Minecraft mc = Minecraft.getMinecraft();
		RayTraceResult objectHit   = mc.objectMouseOver;
		int            blockDamage = 0;
		boolean clearSign = true;
		if (objectHit != null && objectHit.typeOfHit == RayTraceResult.Type.ENTITY && objectHit.entityHit != null) {
			BlockPos entityPosition = objectHit.entityHit.getPosition();
			
			this.storeVariable("EHITX", entityPosition.getX());
			this.storeVariable("EHITY", entityPosition.getY());
			this.storeVariable("EHITZ", entityPosition.getZ());
		} else {
			this.storeVariable("EHITX", 0);
			this.storeVariable("EHITY", 0);
			this.storeVariable("EHITZ", 0);
		}
	}
	
	@Override
	public void onInit() {
		
		ScriptContext.MAIN.getCore().registerVariableProvider(this);
	}
	
	@Override
	public Object getVariable(String variableName) {
		return this.getCachedValue(variableName);
	}
}
