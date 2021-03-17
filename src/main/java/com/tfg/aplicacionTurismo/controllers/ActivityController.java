package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.activity.ActivityDTO;
import com.tfg.aplicacionTurismo.DTO.activity.ActivitySendDTO;
import com.tfg.aplicacionTurismo.DTO.Mensaje;
import com.tfg.aplicacionTurismo.DTO.activity.ImageDTO;
import com.tfg.aplicacionTurismo.entities.*;
import com.tfg.aplicacionTurismo.files.FileUploadUtil;
import com.tfg.aplicacionTurismo.services.ActivityService;
import com.tfg.aplicacionTurismo.services.CityService;
import com.tfg.aplicacionTurismo.services.InterestService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Base64;
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
    public ResponseEntity<List<ActivitySendDTO>> getListado() throws IOException {
        List<Activity> listActivity = activityService.getActivities();
        List<ActivitySendDTO> listDTO = new ArrayList<>();
        for(Activity activity: listActivity){
            ActivitySendDTO i = new ActivitySendDTO(activity.getId(), activity.getName(), activity.getDescription(), activity.getCoordenates().getX(), activity.getCoordenates().getY(), activity.getPathImage(), activity.getCity().getNameCity(), activity.getInterest().getNameInterest(), activity.getAddress(), getImageFromActivity(activity));

            // Add the activity
            listDTO.add(i);
        }
        return new ResponseEntity<List<ActivitySendDTO>>(listDTO, HttpStatus.OK);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ActivitySendDTO> getActivity(@PathVariable Long id) throws IOException {
        if(!activityService.existsById(id)){
            return new ResponseEntity(new Mensaje("La actividad con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        }
        Activity activity = activityService.getById(id);

        ActivitySendDTO activitySendDTO = new ActivitySendDTO(activity.getId(), activity.getName(), activity.getDescription(), activity.getCoordenates().getX(), activity.getCoordenates().getY(), activity.getPathImage(), activity.getCity().getNameCity(), activity.getInterest().getNameInterest(), activity.getAddress(), this.getImageFromActivity(activity));
        return new ResponseEntity<ActivitySendDTO>(activitySendDTO, HttpStatus.OK);
    }

    private ImageDTO getImageFromActivity(Activity activity) throws IOException {
        // Return the image
        String[] stringSplitThePathImage = activity.getPathImage().split("\\.");
        ImageDTO imageDTO = new ImageDTO();

        // Set the fileName
        imageDTO.setFileName(stringSplitThePathImage[0]);

        // Set the mimeType
        imageDTO.setMimeType("image/" + stringSplitThePathImage[1]);

        // Set the data
        String pathName = "/Users/alba-/Desktop/photos/" + activity.getPathImage();
        byte[] fileContent = FileUtils.readFileToByteArray(new File(pathName));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        imageDTO.setData(encodedString);

        return imageDTO;
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
        fileName = stripDiacritics(fileName);
        Activity activity = new Activity(activityDTO.getName(), activityDTO.getDescription(), new Point(activityDTO.getLongitude(), activityDTO.getLatitude()), fileName, activityDTO.getAddress());
        City city = cityService.getCityByNameCity(activityDTO.getCity());
        activity.setCity(city);
        Interest interest = interestService.getInterestByName(activityDTO.getInterest());
        activity.setInterest(interest);
        activityService.addActivities(activity);
        /*String uploadDir = "aplicacionTurismo/src/main/resources/static/images/" + activity.getName();
        //String uploadDir = "C:\\Users\\alba-\\Desktop\\Images\\" + activity.getName();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);*/

        String folder = "/Users/alba-/Desktop/photos/";
        //byte[] bytes = multipartFile.getBytes();
        //Path path = Paths.get(folder + multipartFile.getOriginalFilename());
        //Files.write(path, bytes);
        FileUploadUtil.saveFile(folder, fileName, multipartFile);
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
    public ResponseEntity<?> deleteActivity(@PathVariable Long id) {
        if(!activityService.existsById(id)){
            return new ResponseEntity<>(new Mensaje("La actividad con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        }
        //NOTE: Comprobar que no tiene usuarios asociados
        Activity activity = activityService.getById(id);
        String pathImage = "/Users/alba-/Desktop/photos/" + activity.getPathImage();
        FileUploadUtil.removeFile(pathImage);
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

        String previousPath = activity.getPathImage();

        activity.setName(activityDTO.getName());
        Interest interest = interestService.getInterestByName(activityDTO.getInterest());
        activity.setInterest(interest);
        activity.setDescription(activityDTO.getDescription());
        activity.setCoordenates(new Point(activityDTO.getLongitude(), activityDTO.getLatitude()));
        activity.setAddress(activityDTO.getAddress());
        City city = cityService.getCityByNameCity(activityDTO.getCity());
        activity.setCity(city);

        String fileName = org.springframework.util.StringUtils.cleanPath(multipartFile.getOriginalFilename());
        fileName = stripDiacritics(fileName);
        activity.setPathImage(fileName);

        //Eliminamos el archivo
        String downloadDir = "/Users/alba-/Desktop/photos/" + previousPath;
        FileUploadUtil.removeFile(downloadDir);

        //Añadimos el nuevo archivo
        String uploadDir = "/Users/alba-/Desktop/photos/";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        activityService.updateActivity(activity);

        return new ResponseEntity<>(new Mensaje("Actividad actualizada"), HttpStatus.CREATED);
    }
}
