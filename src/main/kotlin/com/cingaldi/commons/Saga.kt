package com.cingaldi.commons

import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class Saga {

    @Transient
    private val commands = ArrayList<Any>()

    private var completed : Boolean = false

    protected fun dispatchCommand(command: Any)  {
        this.commands.add(command)
    }

    protected fun complete() {
        this.completed = true
    }

    fun commands() : List<Any> {
        return this.commands
    }

    fun clearCommands() {
        this.commands.clear()
    }

    fun isCompleted(): Boolean {
        return completed
    }
}