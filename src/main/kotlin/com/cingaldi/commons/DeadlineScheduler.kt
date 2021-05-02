package com.cingaldi.commons

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 *  notice! this way to schedule a delayed task is not safe at all!
 *  the scheduling won't survive to a service reboot
 *
 *  accepts two callbacks that define how to retrieve the saga and what action to perform next
 *
 *  @return a Job to cancel the deadline.
 *
 *  TODO: give the deadline a name and memorize the job in a dictionary in order to fetch e deadline job by name
 */
fun <T : Saga> scheduleDeadline(delayMillis: Long, findSaga: () -> Optional<T>, perform: (T) -> Unit) : Job {
    return GlobalScope.launch {
        delay(delayMillis)
        findSaga().ifPresent{ saga ->
            if(!saga.isCompleted()) {
                perform(saga)
            }
        }
    }
}