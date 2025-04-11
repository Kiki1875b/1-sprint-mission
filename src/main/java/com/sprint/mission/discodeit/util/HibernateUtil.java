package com.sprint.mission.discodeit.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.internal.StatefulPersistenceContext;
import org.hibernate.engine.spi.EntityHolder;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HibernateUtil {

  private static EntityManager em;

  @PersistenceContext
  public void setEntityManager(EntityManager em) {
    HibernateUtil.em = em;
  }

  public static void printPersistenceContext() {
    SessionImplementor sessionImplementor = em.unwrap(SessionImplementor.class);
    org.hibernate.engine.spi.PersistenceContext persistenceContext = sessionImplementor.getPersistenceContext();

    StatefulPersistenceContext spc = (StatefulPersistenceContext) persistenceContext;
    Map<EntityKey, EntityHolder> m2 = spc.getEntityHoldersByKey();

    for (EntityKey v : m2.keySet()) {
      log.debug("[Persistence Context][Entity] :{}  [Initialized] : {}", v,
          m2.get(v).isInitialized());
    }

    log.debug("[SIZE] : {}", m2.size());

  }
}
