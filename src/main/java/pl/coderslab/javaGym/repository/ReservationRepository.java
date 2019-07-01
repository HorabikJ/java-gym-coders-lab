package pl.coderslab.javaGym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.javaGym.entity.data.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {



}
