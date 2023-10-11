package com.project.todayWhatToDo.notify.service;

import com.project.todayWhatToDo.notify.domain.Notify;
import com.project.todayWhatToDo.notify.dto.CheckNotifyRequestDto;
import com.project.todayWhatToDo.notify.dto.DeleteNotifyRequestDto;
import com.project.todayWhatToDo.notify.dto.GetNotifyRequestDto;
import com.project.todayWhatToDo.notify.dto.NotifyDto;
import com.project.todayWhatToDo.notify.repository.NotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotifyService {

    private final NotifyRepository notifyRepository;

    public Page<NotifyDto> getNotifies(GetNotifyRequestDto request, Pageable pageable){
        return notifyRepository.findByUserId(request.userId(), pageable)
                .map(Notify::toDto);
    }

    public void delete(DeleteNotifyRequestDto request){
        notifyRepository.deleteAllByIdInAndUserId(request.notifyIds(), request.userId());
    }

    public void checkNotify(CheckNotifyRequestDto request){
        var notify = notifyRepository.findById(request.id()).orElseThrow(NotifyNotFoundException::new);
        notify.check();
    }
}