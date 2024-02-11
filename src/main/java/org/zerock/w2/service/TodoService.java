package org.zerock.w2.service;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.zerock.w2.dao.TodoDAO;
import org.zerock.w2.domain.TodoVo;
import org.zerock.w2.dto.TodoDTO;
import org.zerock.w2.util.MapperUtil;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public enum TodoService {
    INSTANCE;

    private TodoDAO dao;
    private ModelMapper modelMapper;

    TodoService() {

        dao = new TodoDAO();
        modelMapper = MapperUtil.INSTANCE.get();
    }

    public void register(TodoDTO todoDTO) throws Exception {

        TodoVo todoVo = modelMapper.map(todoDTO, TodoVo.class);

        //System.out.println("todoVO: " + todoVo);
        log.info(todoVo);

        dao.insert(todoVo); //int를 변환하므로 이를 이용해서 예외처리도 가능
    }

    public List<TodoDTO> listAll() throws Exception {

        List<TodoVo> voList = dao.selectAll();

        log.info("voList........");
        log.info(voList);

        List<TodoDTO> dtoList = voList.stream()
                .map(vo -> modelMapper.map(vo, TodoDTO.class))
                .collect(Collectors.toList());

        return dtoList;
    }

    public TodoDTO get(Long tno) throws Exception {

        log.info("tno: " + tno);
        TodoVo todoVo = dao.selectOne(tno);
        TodoDTO todoDTO = modelMapper.map(todoVo, TodoDTO.class);
        return todoDTO;

    }

    public void remove(Long tno) throws Exception {
        log.info("tno: "+tno);
        dao.deleteOne(tno);
    }

    public void modify(TodoDTO todoDTO) throws Exception {

        log.info("todoDTO: " + todoDTO);

        TodoVo todoVo = modelMapper.map(todoDTO, TodoVo.class);

        dao.updateOne(todoVo);
    }


}
