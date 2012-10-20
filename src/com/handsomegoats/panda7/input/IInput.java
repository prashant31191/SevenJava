package com.handsomegoats.panda7.input;

import com.handsomegoats.panda7.*;
import com.handsomegoats.panda7.view.*;

public interface IInput {

	public void touchDown(AController controller, IView view, float x, float y);
	public void touchMove(AController controller, IView view, float x, float y);
	public void touchPress(AController controller, IView view, float x, float y);
}
