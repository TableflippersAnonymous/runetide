package com.runetide.services.internal.resourcepool.server.domain;

import com.runetide.common.Constants;
import com.runetide.common.services.topics.TopicManager;
import com.runetide.services.internal.resourcepool.common.ResourcePool;
import com.runetide.services.internal.resourcepool.common.ResourcePoolEffect;
import com.runetide.services.internal.resourcepool.common.ResourcePoolTransactMessage;
import com.runetide.services.internal.resourcepool.common.ResourcePoolUpdateMessage;
import com.runetide.services.internal.resourcepool.server.dao.ResourcePoolDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LoadedResourcePool {
    private final ResourcePool resourcePool;
    private final ResourcePoolDao dao;
    private final TopicManager topicManager;

    public LoadedResourcePool(ResourcePool resourcePool, ResourcePoolDao dao, TopicManager topicManager) {
        this.resourcePool = resourcePool;
        this.dao = dao;
        this.topicManager = topicManager;
    }

    public synchronized void tick(long tick) {
        final List<String> effectsToRemove = new ArrayList<>();
        for(final Map.Entry<String, ResourcePoolEffect> entry : resourcePool.getEffects().entrySet()) {
            final ResourcePoolEffect effect = entry.getValue();
            if(tick % effect.getInterval() != 0)
                continue;

            final boolean updated = transact(effect.getDelta(), effect.isIgnoreNormalLimits(), effect.isTakePartial(),
                    effect.getOverrideMin(), effect.getOverrideMax());

            if(effect.getRepetitions() != -1)
                effect.setRepetitions(effect.getRepetitions() - 1);

            if(effect.getRepetitions() == 0 || (!effect.isPersistent() && !updated))
                effectsToRemove.add(entry.getKey());
        }

        if(effectsToRemove.isEmpty())
            return;

        for(final String key : effectsToRemove)
            resourcePool.getEffects().remove(key);

        save();
    }

    public void save() {
        dao.save(resourcePool);
    }

    public synchronized void addEffect(String name, ResourcePoolEffect effect) {
        resourcePool.getEffects().put(name, effect);
        save();
    }

    public synchronized void deleteEffect(String name) {
        resourcePool.getEffects().remove(name);
        save();
    }

    public synchronized boolean transact(long delta, boolean ignoreNormal, boolean takePartial,
                                         Long overrideMin, Long overrideMax) {
        final long normalLow = Optional.ofNullable(overrideMin).orElse(resourcePool.getNormalLimitLower());
        final long normalHigh = Optional.ofNullable(overrideMax).orElse(resourcePool.getNormalLimitUpper());
        final long lowLimit = ignoreNormal ? resourcePool.getFinalLimitLower() : normalLow;
        final long highLimit = ignoreNormal ? resourcePool.getFinalLimitUpper() : normalHigh;
        final long oldValue = resourcePool.getValue();
        final long newRawValue = oldValue + delta;

        if(!takePartial && (newRawValue < lowLimit || newRawValue > highLimit))
            return false;

        final long newValue = Math.max(Math.min(newRawValue, highLimit), lowLimit);
        resourcePool.setValue(newValue);
        save();
        topicManager.publish(Constants.RESOURCE_POOL_TOPIC_PREFIX + resourcePool.getId() + ":transact",
                new ResourcePoolTransactMessage(resourcePool.getId(), oldValue, newValue));
        return newRawValue == newValue;
    }

    public ResourcePool getResourcePool() {
        return resourcePool;
    }

    public synchronized void update(ResourcePool resourcePool) {
        this.resourcePool.setValue(resourcePool.getValue());
        this.resourcePool.setNormalLimitLower(resourcePool.getNormalLimitLower());
        this.resourcePool.setNormalLimitUpper(resourcePool.getNormalLimitUpper());
        this.resourcePool.setFinalLimitLower(resourcePool.getFinalLimitLower());
        this.resourcePool.setFinalLimitUpper(resourcePool.getFinalLimitUpper());
        if(resourcePool.getEffects() != null)
            this.resourcePool.setEffects(resourcePool.getEffects());
        save();
        topicManager.publish(Constants.RESOURCE_POOL_TOPIC_PREFIX + resourcePool.getId() + ":update",
                new ResourcePoolUpdateMessage(resourcePool));
    }
}
