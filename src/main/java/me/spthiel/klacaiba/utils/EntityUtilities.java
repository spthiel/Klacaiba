package me.spthiel.klacaiba.utils;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class EntityUtilities {
	
	static final Predicate<Entity> TRACEABLE = Predicates.and(EntitySelectors.NOT_SPECTATING, entity -> entity != null && entity.canBeCollidedWith());
	
	static final class EntityTrace {
		
		Entity entity;
		Vec3d  location;
		double distance;
		
		EntityTrace(double entityDistance) {
			
			this.distance = entityDistance;
		}
		
		RayTraceResult asRayTraceResult() {
			
			return new RayTraceResult(this.entity, this.location);
		}
	}
	
	public static RayTraceResult rayTraceFromEntity(Entity source, double traceDistance, float partialTicks, float yaw, float pitch, boolean includeEntities) {
		
		RayTraceResult blockRay = rayTraceFromEntity(source, traceDistance, partialTicks, yaw, pitch);
		
		if (!includeEntities) {
			return blockRay;
		}
		
		Vec3d       traceStart    = getPositionEyes(source, partialTicks);
		double      blockDistance = (blockRay != null) ? blockRay.hitVec.distanceTo(traceStart) : traceDistance;
		EntityTrace entityRay     = rayTraceEntities(source, traceDistance, partialTicks, blockDistance, traceStart, yaw, pitch);
		
		if (entityRay.entity != null && (entityRay.distance < blockDistance || blockRay == null)) {
			return entityRay.asRayTraceResult();
		}
		
		return blockRay;
	}
	
	private static EntityTrace rayTraceEntities(Entity source, double traceDistance, float partialTicks, double blockDistance, Vec3d traceStart, float yaw, float pitch) {
		
		EntityTrace trace = new EntityTrace(blockDistance);
		
		Vec3d lookDir  = getVectorForRotation(yaw, pitch)
							   .scale(traceDistance);
		Vec3d traceEnd = traceStart.add(lookDir);
		
		for (final Entity entity : getTraceEntities(source, traceDistance, lookDir, TRACEABLE)) {
			AxisAlignedBB  entityBB  = entity.getEntityBoundingBox()
											 .grow(entity.getCollisionBorderSize());
			RayTraceResult entityRay = entityBB.calculateIntercept(traceStart, traceEnd);
			
			if (entityBB.contains(traceStart)) {
				if (trace.distance >= 0.0D) {
					trace.entity = entity;
					trace.location = entityRay == null ? traceStart : entityRay.hitVec;
					trace.distance = 0.0D;
				}
				continue;
			}
			
			if (entityRay == null) {
				continue;
			}
			
			double distanceToEntity = traceStart.distanceTo(entityRay.hitVec);
			
			if (distanceToEntity < trace.distance || trace.distance == 0.0D) {
				if (entity.getLowestRidingEntity() == source.getLowestRidingEntity()) {
					if (trace.distance == 0.0D) {
						trace.entity = entity;
						trace.location = entityRay.hitVec;
					}
				} else {
					trace.entity = entity;
					trace.location = entityRay.hitVec;
					trace.distance = distanceToEntity;
				}
			}
		}
		
		return trace;
	}
	
	private static List<Entity> getTraceEntities(Entity source, double traceDistance, Vec3d dir, Predicate<Entity> filter) {
		
		AxisAlignedBB boundingBox = source.getEntityBoundingBox();
		AxisAlignedBB traceBox    = boundingBox.expand(dir.x, dir.y, dir.z);
		return source.world.getEntitiesInAABBexcluding(source, traceBox.expand(1.0F, 1.0F, 1.0F), filter);
	}
	
	public static RayTraceResult rayTraceFromEntity(Entity source, double traceDistance, float partialTicks, float yaw, float pitch) {
		
		Vec3d traceStart = getPositionEyes(source, partialTicks);
		Vec3d lookDir    = getVectorForRotation(yaw, pitch)
								 .scale(traceDistance);
		Vec3d traceEnd   = traceStart.add(lookDir);
		return source.world.rayTraceBlocks(traceStart, traceEnd, false, false, true);
	}
	
	public static Vec3d getPositionEyes(Entity entity, float partialTicks) {
		
		if (partialTicks == 1.0F) {
			return new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
		}
		
		double interpX = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
		double interpY = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + entity.getEyeHeight();
		double interpZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
		return new Vec3d(interpX, interpY, interpZ);
	}
	
	protected static Vec3d getVectorForRotation(float yaw, float pitch)
	{
		float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
		float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
		float f2 = -MathHelper.cos(-pitch * 0.017453292F);
		float f3 = MathHelper.sin(-pitch * 0.017453292F);
		return new Vec3d(f1 * f2, f3, f * f2);
	}
}
