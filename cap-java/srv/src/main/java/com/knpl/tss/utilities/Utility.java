package com.knpl.tss.utilities;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knpl.tss.exceptions.UUIDNotFoundException;

import org.springframework.stereotype.Component;

@Component
public class Utility {
    
    final ObjectMapper objectMapper = new ObjectMapper();

    private List<Map<String, List<String>>> parseCQNRefValue(String jsonRefString) throws JsonMappingException, JsonProcessingException {
			List<Map<String, List<String>>> list = objectMapper.readValue(jsonRefString, List.class);
            return list;
    }

    public String getUUIDFromCQNRef(String jsonRefString) throws UUIDNotFoundException {
        List<Map<String, List<String>>> parsedValues;
        try {
            parsedValues = this.parseCQNRefValue(jsonRefString);
            Map<String, List<String>> cqnRefValueMap = parsedValues.get(4);
			List<String> refKeys = cqnRefValueMap.get("ref");
            if(!refKeys.contains("ID")) {
                throw new UUIDNotFoundException("UUID not found");
            }
            Map<String, List<String>> cqnRefValueMap2 = parsedValues.get(6);
            return String.valueOf(cqnRefValueMap2.get("val"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new UUIDNotFoundException("UUID not found");
        }
    }
}
