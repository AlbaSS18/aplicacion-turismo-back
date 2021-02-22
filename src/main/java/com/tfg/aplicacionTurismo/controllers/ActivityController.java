package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.activity.ActivityDTO;
import com.tfg.aplicacionTurismo.DTO.activity.ActivitySendDTO;
import com.tfg.aplicacionTurismo.DTO.Mensaje;
import com.tfg.aplicacionTurismo.entities.*;
import com.tfg.aplicacionTurismo.files.FileUploadUtil;
import com.tfg.aplicacionTurismo.services.ActivityService;
import com.tfg.aplicacionTurismo.services.CityService;
import com.tfg.aplicacionTurismo.services.InterestService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private CityService cityService;

    @Autowired
    private InterestService interestService;

    @GetMapping("/list")
    public ResponseEntity<List<ActivitySendDTO>> getListado(){
        List<Activity> listActivity = activityService.getActivities();
        List<ActivitySendDTO> listDTO = new ArrayList<>();
        for(Activity activity: listActivity){
            ActivitySendDTO i = new ActivitySendDTO(activity.getId(), activity.getName(), activity.getDescription(), activity.getCoordenates().getX(), activity.getCoordenates().getY(), activity.getPathImage(), activity.getCity().getNameCity(), activity.getInterest().getNameInterest(), activity.getAddress());
            listDTO.add(i);
        }
        return new ResponseEntity<List<ActivitySendDTO>>(listDTO, HttpStatus.OK);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ActivitySendDTO> getActivity(@PathVariable Long id){
        if(!activityService.existsById(id)){
            return new ResponseEntity(new Mensaje("La actividad con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        }
        Activity activity = activityService.getById(id);
        ActivitySendDTO activitySendDTO = new ActivitySendDTO(activity.getId(), activity.getName(), activity.getDescription(), activity.getCoordenates().getX(), activity.getCoordenates().getY(), activity.getPathImage(), activity.getCity().getNameCity(), activity.getInterest().getNameInterest(), activity.getAddress());
        return new ResponseEntity<ActivitySendDTO>(activitySendDTO, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addActivity(@RequestParam(name="image", required = false) MultipartFile multipartFile, ActivityDTO activityDTO ) throws IOException {
        if(multipartFile == null ){
            return new ResponseEntity<>(new Mensaje("El archivo de imagen es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        System.out.println(activityDTO.getAddress());
        if(StringUtils.isEmpty(activityDTO.getName())){
            return new ResponseEntity<>(new Mensaje("El nombre de la actividad es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isEmpty(activityDTO.getDescription())){
            return new ResponseEntity<>(new Mensaje("La descripción es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isEmpty(activityDTO.getCity())){
            return new ResponseEntity<>(new Mensaje("El nombre de la ciudad es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(!cityService.existByName(activityDTO.getCity())){
            return new ResponseEntity<>(new Mensaje("La ciudad no existe"), HttpStatus.NOT_FOUND);
        }
        if(StringUtils.isEmpty(activityDTO.getInterest())){
            return new ResponseEntity<>(new Mensaje("El nombre del interés es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(!interestService.existByName(activityDTO.getInterest())){
            return new ResponseEntity<>(new Mensaje("El interés no existe"), HttpStatus.NOT_FOUND);
        }
        if(StringUtils.isEmpty(activityDTO.getAddress())){
            return new ResponseEntity<>(new Mensaje("La dirección es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        if(activityService.existsByName(activityDTO.getName())){
            return new ResponseEntity<>(new Mensaje("Ya existe una actividad con el nombre: " + activityDTO.getName()), HttpStatus.BAD_REQUEST);
        }

        // NOTE: No compruebo las coordenadas porque puede haber coordenadas igual a cero.

        String fileName = org.springframework.util.StringUtils.cleanPath(multipartFile.getOriginalFilename());
        System.out.println(fileName);
        fileName = stripDiacritics(fileName);
        Activity activity = new Activity(activityDTO.getName(), activityDTO.getDescription(), new Point(activityDTO.getLongitude(), activityDTO.getLatitude()), fileName, activityDTO.getAddress());
        City city = cityService.getCityByNameCity(activityDTO.getCity());
        activity.setCity(city);
        Interest interest = interestService.getInterestByName(activityDTO.getInterest());
        activity.setInterest(interest);
        activityService.addActivities(activity);
        String uploadDir = "aplicacionTurismo/src/main/resources/static/images/" + activity.getName();
        //String uploadDir = "C:\\Users\\alba-\\Desktop\\Images\\" + activity.getName();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        return new ResponseEntity<>(new Mensaje("Actividad creada"), HttpStatus.CREATED);
    }

    public static final Pattern DIACRITICS_AND_FRIENDS
            = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

    private static String stripDiacritics(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = DIACRITICS_AND_FRIENDS.matcher(str).replaceAll("");
        return str;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteActivity(@PathVariable Long id) throws IOException {
        if(!activityService.existsById(id)){
            return new ResponseEntity<>(new Mensaje("La actividad con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        }
        //NOTE: Comprobar que no tiene usuarios asociados
        Activity activity = activityService.getById(id);
        String uploadDir = "aplicacionTurismo/src/main/resources/static/images/" + activity.getName();
        FileUploadUtil.removeFile(uploadDir,activity.getPathImage());
        activityService.removeActivities(id);

        return new ResponseEntity<>(new Mensaje("Actividad eliminada"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateActivity(@RequestParam("image") MultipartFile multipartFile, ActivityDTO activityDTO, @PathVariable Long id) throws IOException {
        if(activityService.existsByName(activityDTO.getName()) && activityService.getActivityByNameActivity(activityDTO.getName()).getId() != id){
            return new ResponseEntity(new Mensaje("Ya hay una actividad con ese nombre"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isEmpty(activityDTO.getName())){
            return new ResponseEntity<>(new Mensaje("El nombre de la actividad es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isEmpty(activityDTO.getDescription())){
            return new ResponseEntity<>(new Mensaje("La descripción de la actividad es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isEmpty(activityDTO.getCity())){
            return new ResponseEntity<>(new Mensaje("La ciudad es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        if(!cityService.existByName(activityDTO.getCity())){
            return new ResponseEntity<>(new Mensaje("La ciudad no existe"), HttpStatus.NOT_FOUND);
        }
        if(StringUtils.isEmpty(activityDTO.getInterest())){
            return new ResponseEntity<>(new Mensaje("El nombre del interés es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(!interestService.existByName(activityDTO.getInterest())){
            return new ResponseEntity<>(new Mensaje("El interés no existe"), HttpStatus.NOT_FOUND);
        }
        if(StringUtils.isEmpty(activityDTO.getAddress())){
            return new ResponseEntity<>(new Mensaje("La dirección es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        // NOTE: No compruebo las coordenadas porque puede haber coordenadas igual a cero.

        Activity activity = activityService.getById(id);
        Interest interest = interestService.getInterestByName(activityDTO.getInterest());
        activity.setInterest(interest);
        activity.setDescription(activityDTO.getDescription());
        activity.setCoordenates(new Point(activityDTO.getLongitude(), activityDTO.getLatitude()));
        activity.setAddress(activityDTO.getAddress());
        City city = cityService.getCityByNameCity(activityDTO.getCity());
        activity.setCity(city);

        String fileName = org.springframework.util.StringUtils.cleanPath(multipartFile.getOriginalFilename());
        System.out.println(fileName);
        fileName = stripDiacritics(fileName);
        activity.setPathImage(fileName);
        activityService.updateActivity(activity);

        //Eliminamos el archivo
        String downloadDir = "aplicacionTurismo/src/main/resources/static/images/" + activity.getName();
        FileUploadUtil.removeFile(downloadDir,activity.getPathImage());

        //Añadimos el nuevo archivo
        String uploadDir = "aplicacionTurismo/src/main/resources/static/images/" + activity.getName();
        //String uploadDir = "C:\\Users\\alba-\\Desktop\\Images\\" + activity.getName();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        return new ResponseEntity<>(new Mensaje("Actividad actualizada"), HttpStatus.CREATED);
    }

    @GetMapping("/picture/{id}")
    public ResponseEntity<byte[]> getArticleImage(@PathVariable Long id) throws IOException {

        // 1. download img from http://internal-picture-db/id.jpg ...
        Activity activity = activityService.getById(id);
        String pathName = "aplicacionTurismo/src/main/resources/static/images/" + activity.getName() + "/" + activity.getPathImage();
        //FileSystemResource imgFile = new FileSystemResource(name);


        byte[] image = org.apache.commons.io.FileUtils.readFileToByteArray(new File(pathName));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(image.length);


        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}
