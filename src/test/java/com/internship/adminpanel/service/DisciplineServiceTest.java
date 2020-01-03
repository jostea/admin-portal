package com.internship.adminpanel.service;

import com.internship.adminpanel.exception.DisciplineNotFound;
import com.internship.adminpanel.exception.EmptyName;
import com.internship.adminpanel.model.Discipline;
import com.internship.adminpanel.model.dto.discipline.DisciplineDTO;
import com.internship.adminpanel.repository.DisciplineRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DisciplineServiceTest {

    @Mock
    private DisciplineRepository disciplineRepository;

    @InjectMocks
    private DisciplineService disciplineService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldFindAllDiscipline() throws Exception {
        List<DisciplineDTO> expectedDisciplineList = new ArrayList<>();
        expectedDisciplineList.add(new DisciplineDTO(createDiscipline("TestDiscipline1")));
        expectedDisciplineList.add(new DisciplineDTO(createDiscipline("TestDiscipline2")));
        List<Discipline> mockedDbDiscipline = new ArrayList<>();
        mockedDbDiscipline.add(createDiscipline("TestDiscipline1"));
        mockedDbDiscipline.add(createDiscipline("TestDiscipline2"));
        when(disciplineRepository.findAll()).thenReturn(mockedDbDiscipline);
        List<DisciplineDTO> returnedDisciplineList = disciplineService.getAllDisciplines();
        verify(disciplineRepository).findAll();
        assertThat(returnedDisciplineList).isEqualTo(expectedDisciplineList);
    }

    @Test
    public void shouldReturnedDisciplinedSpecifiedById() throws DisciplineNotFound {
        Long id = 1L;
        Discipline discipline = createDiscipline("MangalShashlyk");
        DisciplineDTO expectedDisciplineDTO = new DisciplineDTO(discipline);
        when(disciplineRepository.findById(id)).thenReturn(Optional.of(discipline));
        DisciplineDTO returnedDisciplineDTO = disciplineService.findById(id);
        verify(disciplineRepository, times(2)).findById(id);
        assertThat(expectedDisciplineDTO).isEqualTo(returnedDisciplineDTO);
    }

    @Test
    public void shouldReturnedDisciplineListSpecifiedByName() throws DisciplineNotFound {
        List<DisciplineDTO> expectedDisciplineList = new ArrayList<>();
        expectedDisciplineList.add(new DisciplineDTO(createDiscipline("TestDiscipline1")));
        expectedDisciplineList.add(new DisciplineDTO(createDiscipline("TestDiscipline2")));
        List<Discipline> mockedDbDiscipline = new ArrayList<>();
        mockedDbDiscipline.add(createDiscipline("TestDiscipline1"));
        mockedDbDiscipline.add(createDiscipline("TestDiscipline2"));
        String name = "e";
        when(disciplineRepository.findDisciplineByNameContainingIgnoreCase(name))
                .thenReturn(mockedDbDiscipline);
        List<DisciplineDTO> returnedDisciplineList = disciplineService.filterByName(name);
        verify(disciplineRepository).findDisciplineByNameContainingIgnoreCase(name);
        assertThat(returnedDisciplineList).isEqualTo(expectedDisciplineList);
    }

    @Test
    public void shouldDeleteDiscipline() throws Exception {
        Long id = 1L;
        disciplineService.delete(id);
        verify(disciplineRepository).deleteById(id);
    }

    @Test
    public void shouldUpdateStream() throws DisciplineNotFound, EmptyName {
        Long id = 1L;
        Discipline mockedDiscipline = createDiscipline("TestDiscipline");
        DisciplineDTO disciplineDTO = createDisciplineDTO("TestDisciplineUpdated");
        String username = "TestUsername";
        when(disciplineRepository.findById(id)).thenReturn(Optional.of(mockedDiscipline));
        disciplineService.edit(id, disciplineDTO, username);
        verify(disciplineRepository).findById(id);
        verify(disciplineRepository).save(mockedDiscipline);
    }

    @Test
    public void shouldSaveDiscipline() throws Exception {
        DisciplineDTO disciplineDTO = createDisciplineDTO("NewDisciplineTest");
        Discipline discipline = createDisciplineFromDTO(disciplineDTO);
        disciplineService.add(disciplineDTO);
        verify(disciplineRepository).save(discipline);
    }

    @Test
    public void shouldTrowExceptionWhileSearchingDisciplineById() throws DisciplineNotFound {
        Long id = 100L;
        exception.expect(DisciplineNotFound.class);
        exception.expectMessage(id + "");
        when(disciplineRepository.findById(id)).thenReturn(Optional.empty());
        disciplineService.findById(id);
        verify(disciplineRepository).findById(id);
    }

    @Test(expected = EmptyName.class)
    public void shouldThrowExceptionEmptyNameWhileEditDiscipline() throws DisciplineNotFound, EmptyName {
        Long id = 1L;
        DisciplineDTO disciplineDTO = createDisciplineDTO("");
        Discipline discipline = createDiscipline("TestName");
        when(disciplineRepository.findById(id)).thenReturn(Optional.of(discipline));
        disciplineService.edit(id, disciplineDTO, null);
        verify(disciplineRepository).findById(id);
    }

    @Test(expected = EmptyName.class)
    public void shouldThrowExceptionEmptyNameWhileAddDiscipline() throws Exception {
        DisciplineDTO disciplineDTO = createDisciplineDTO("");
        disciplineService.add(disciplineDTO);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhileAddDiscipline() throws Exception {
        DisciplineDTO disciplineDTO = createDisciplineDTO("TestDisciplineDTO");
        Discipline discipline = createDisciplineFromDTO(disciplineDTO);
        doThrow(new Exception()).when(disciplineRepository).save(discipline);
        disciplineService.add(disciplineDTO);
        verify(disciplineRepository).save(discipline);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhileDeleteDiscipline() throws Exception {
        Long id = 1L;
        doThrow(new Exception()).when(disciplineService).delete(id);
        disciplineService.delete(id);
        verify(disciplineRepository).deleteById(id);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhileGetAllDisciplines() throws Exception {
        when(disciplineRepository.findAll()).thenThrow(new Exception());
        disciplineService.getAllDisciplines();
        verify(disciplineRepository).findAll();
    }
    @Test
    public void shouldThrowExceptionWhileEditDiscipline() throws DisciplineNotFound, EmptyName {
        Long id = 1L;
        exception.expect(DisciplineNotFound.class);
        exception.expectMessage(id + "");
        DisciplineDTO disciplineDTO = createDisciplineDTO("TestDisciplineUpdated");
        String username = "TestUsername";
        when(disciplineRepository.findById(id)).thenReturn(Optional.empty());
        disciplineService.edit(id, disciplineDTO, username);
    }

    private Discipline createDiscipline(String name) {
        return Discipline.builder()
                .id(1L)
                .name(name)
                .build();
    }

    private DisciplineDTO createDisciplineDTO(String name) {
        return DisciplineDTO.builder()
                .id(1L)
                .name(name)
                .build();
    }

    private Discipline createDisciplineFromDTO(DisciplineDTO disciplineDTO) {
        return Discipline.builder()
                .name(disciplineDTO.getName())
                .build();
    }
}
