package com.example.mainservice.storage;

import com.example.mainservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {

}
