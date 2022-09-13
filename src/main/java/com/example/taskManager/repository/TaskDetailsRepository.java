package com.example.taskManager.repository;

import com.example.taskManager.entity.TaskDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDetailsRepository extends JpaRepository<TaskDetails,Integer> {
}
