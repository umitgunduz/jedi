/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.common;

/**
 * @author umitgunduz
 */
public interface ExecutableComponent<T extends ComponentParameters>
        extends Component {

    T execute(T parameters) throws Exception;
}

