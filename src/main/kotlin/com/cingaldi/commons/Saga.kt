package com.cingaldi.commons

abstract class Saga {

    @Transient
    private val commands = ArrayList<Any>()

    protected fun dispatchCommand(command: Any)  {
        this.commands.add(command)
    }

    fun commands() : List<Any> {
        return this.commands
    }
}