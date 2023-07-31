package org.product.repository;

import org.product.model.ApprovalQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalQueueRepository extends JpaRepository<ApprovalQueue,Long> {

}
