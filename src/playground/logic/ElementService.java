package playground.logic;

import playground.elements.ElementTO;

public interface ElementService {
	ElementTO[] getElement(int page, int limit);
}
