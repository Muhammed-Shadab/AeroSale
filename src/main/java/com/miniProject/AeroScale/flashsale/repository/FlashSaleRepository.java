package com.miniProject.AeroScale.flashsale.repository;

import com.miniProject.AeroScale.flashsale.entity.FlashSale;
import com.miniProject.AeroScale.flashsale.entity.FlashSale.FlashSaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlashSaleRepository extends JpaRepository<FlashSale, UUID> {

    // product ki SCHEDULED/ACTIVE sale (ek waqt par ek hi hoti hai)
    Optional<FlashSale> findFirstByProductIdAndStatusIn(UUID productId, Collection<FlashSaleStatus> statuses);

    boolean existsByProductIdAndStatusIn(UUID productId, Collection<FlashSaleStatus> statuses);

    // catalog list ke liye: ek query mein bhot saaaaaare products ki khuli sales
    List<FlashSale> findByProductIdInAndStatusInAndEndTimeAfter(
            Collection<UUID> productIds, Collection<FlashSaleStatus> statuses, Instant now);

    // GET /catalog/flash-sales (Step 2B)
    Page<FlashSale> findByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThan(
            FlashSaleStatus status, Instant start, Instant end, Pageable pageable);

    Page<FlashSale> findByStatusAndStartTimeGreaterThan(FlashSaleStatus status, Instant now, Pageable pageable);

    // paid units badhao: sirf us product ki ACTIVE sale par, ek atomic UPDATE mein
    @Modifying
    @Query("update FlashSale f set f.soldQuantity = f.soldQuantity + :qty "
            + "where f.productId = :productId and f.status = :status")
    int addSold(@Param("productId") UUID productId, @Param("qty") int qty,
                @Param("status") FlashSaleStatus status);

    // scheduler: ek hi UPDATE mein sab sales ka status badalta hai
    @Modifying(clearAutomatically = true)
    @Query("update FlashSale f set f.status = :to where f.status in :from and f.endTime <= :now")
    int markEnded(@Param("to") FlashSaleStatus to,
                  @Param("from") Collection<FlashSaleStatus> from,
                  @Param("now") Instant now);

    @Modifying(clearAutomatically = true)
    @Query("update FlashSale f set f.status = :to where f.status = :from and f.startTime <= :now and f.endTime > :now")

    int markActive(@Param("to") FlashSaleStatus to,
                   @Param("from") FlashSaleStatus from,
                   @Param("now") Instant now);
}