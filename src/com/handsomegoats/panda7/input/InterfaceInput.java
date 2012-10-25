package com.handsomegoats.panda7.input;

import com.handsomegoats.panda7.controller.AbstractController;
import com.handsomegoats.panda7.view.*;

public interface InterfaceInput {

	public void touchDown(AbstractController controller, AbstractView view, float x, float y);
	public void touchMove(AbstractController controller, AbstractView view, float x, float y);
	public void touchPress(AbstractController controller, AbstractView view, float x, float y);
}
