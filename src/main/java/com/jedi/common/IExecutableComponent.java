/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.common;

/**
 *
 * @author umitgunduz
 * @param <In>
 * @param <Out>
 */
public interface IExecutableComponent<In extends IInput, Out extends IOutput>
        extends IComponent {

    Out execute(In input) throws Exception;
}
