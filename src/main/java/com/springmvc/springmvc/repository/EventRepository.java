package com.springmvc.springmvc.repository;


import org.springframework.stereotype.Repository;

import com.springmvc.springmvc.models.Event;

@Repository
public interface EventRepository extends BaseRepository<Event,Long> {

}
