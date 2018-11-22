package playground.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.stereotype.Service;

import playground.elements.ElementTO;

@Service
public class ElementServiceImpl implements ElementService {

	@Autowired
	ElementService elementService;
	
    @Override
    public ElementTO[] getElement(int page, int limit) {
        List<ElementTO> returnValue = new ArrayList<>();
        Pageable pageableRequest = PathRequest.of(page, limit);
        Page<ElementTO> users = elementService.findAll(pageableRequest);
        List<ElementTO> userEntities = users.getContent();
        for (ElementTO element : userEntities) {
            ElementTO element2 = new UserDto();
            BeanUtils.copyProperties(element, element2);
            returnValue.add(element2);
        }
        return returnValue.toArray(new ElementTO[returnValue.size()]);
    }

}
