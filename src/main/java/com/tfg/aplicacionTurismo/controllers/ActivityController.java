package com.tfg.aplicacionTurismo.controllers;

import com.tfg.aplicacionTurismo.DTO.activity.*;
import com.tfg.aplicacionTurismo.DTO.Mensaje;
import com.tfg.aplicacionTurismo.entities.*;
import com.tfg.aplicacionTurismo.files.FileUploadUtil;
import com.tfg.aplicacionTurismo.services.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import weka.core.*;
import weka.core.neighboursearch.LinearNNSearch;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;


@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private CityService cityService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private InterestService interestService;

    @Autowired
    private RelUserActivityService relUserActivityService;

    @Autowired
    private RelUserInterestService relUserInterestService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addActivity(@RequestParam(name="image", required = false) MultipartFile multipartFile, @Validated ActivityDTO activityDTO, BindingResult result) throws IOException {
        if(multipartFile == null ){
            return new ResponseEntity<>(new Mensaje("El archivo de imagen es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Formulario inválido"), HttpStatus.BAD_REQUEST);
        }
        if(!cityService.existByName(activityDTO.getCity())){
            return new ResponseEntity<>(new Mensaje("La ciudad no existe"), HttpStatus.NOT_FOUND);
        }
        if(!interestService.existByName(activityDTO.getInterest())){
            return new ResponseEntity<>(new Mensaje("El interés no existe"), HttpStatus.NOT_FOUND);
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

        String folder = "/Users/alba-/Desktop/photos/";
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateActivity(@RequestParam(name = "image", required = false) MultipartFile multipartFile, @Validated ActivityDTO activityDTO,BindingResult result, @PathVariable Long id) throws IOException {
        if(multipartFile == null ){
            return new ResponseEntity<>(new Mensaje("El archivo de imagen es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(activityService.existsByName(activityDTO.getName()) && activityService.getActivityByNameActivity(activityDTO.getName()).getId() != id){
            return new ResponseEntity(new Mensaje("Ya hay una actividad con ese nombre"), HttpStatus.BAD_REQUEST);
        }
        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Formulario inválido"), HttpStatus.BAD_REQUEST);
        }
        if(!cityService.existByName(activityDTO.getCity())){
            return new ResponseEntity<>(new Mensaje("La ciudad no existe"), HttpStatus.NOT_FOUND);
        }
        if(!interestService.existByName(activityDTO.getInterest())){
            return new ResponseEntity<>(new Mensaje("El interés no existe"), HttpStatus.NOT_FOUND);
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

    @GetMapping("/recommedation/{id}")
    public ResponseEntity<List<ActivityRecommendationDTO>> getRecommendedActivities(@PathVariable Long id) throws IOException {
        if(!usersService.existsById(id)){
            return new ResponseEntity(new Mensaje("El usuario con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        }
        User user = usersService.getUserById(id);

        // Data userRatings
        List<Activity> listActivities = activityService.getActivities();

        ArrayList<Attribute> attributesUser = new ArrayList<Attribute>();

        for(Activity activity: listActivities){
            attributesUser.add(new Attribute(activity.getName()));
        }

        Instances dataUser = new Instances("User - activity rating", attributesUser, 0);

        double[] vals = new double[dataUser.numAttributes()];
        int i = 0;
        for(Activity activity: listActivities){

            RelUserActivity relUserActivity = relUserActivityService.getValuationByUserAndActivity(user, activity);

            if(relUserActivity != null){
                vals[i] = relUserActivity.getValuation();
            }
            else{
                vals[i] = 0;
            }
            i++;
        }
        dataUser.add(new DenseInstance(1.0, vals));

        Instance userData = dataUser.firstInstance();

        //System.out.println(userData); ---> 0,0,0,0,0
        //System.out.println(dataUser);

        // Data activityRatings

        ArrayList<Attribute> attributesAllRatingActivities = new ArrayList<Attribute>();

        for(Activity activity: listActivities){
            attributesAllRatingActivities.add(new Attribute(activity.getName()));
        }

        Instances dataRatingActivities = new Instances("Users - activity rating", attributesAllRatingActivities, 0);

        List<User> userList = usersService.getUsers();

        for(User userDB: userList){
            if(userDB.getId() != id){
                int instanceIndex = 0;
                double[] valsUsers = new double[dataRatingActivities.numAttributes()];
                for(Activity activity: listActivities){
                    RelUserActivity relUserActivity = relUserActivityService.getValuationByUserAndActivity(userDB, activity);
                    if(relUserActivity != null){
                        valsUsers[instanceIndex] = relUserActivity.getValuation();
                    }
                    else{
                        valsUsers[instanceIndex] = 0;
                    }
                    instanceIndex++;
                }
                dataRatingActivities.add(new DenseInstance(1.0, valsUsers));
            }
        }

        //System.out.println(dataRatingActivities.toString());
        //System.out.println(dataRatingActivities.toString());


        LinearNNSearch kNN = new LinearNNSearch(dataRatingActivities);
        Instances neighbors = null;
        double[] distances = null;
        try {
            neighbors = kNN.kNearestNeighbours(userData, 5);
            distances = kNN.getDistances();
            //System.out.println(neighbors); --> Instancias
            /*for(double d: distances){
                System.out.println(d); //--> 0.0
            }*/
        }
        catch (Exception e){
            return new ResponseEntity(new Mensaje("Neighbors could not be found"), HttpStatus.NOT_FOUND);
        }

        double[] similarities = new double[distances.length];
        try{
            for(int index = 0; index < distances.length; index++){
                similarities[index] = 1.0 / distances[index];
                //System.out.println(similarities[index]); //Al ser cero arriba, aquí da infinity. Este caso se da cuando no hay vecinos suficientes (mín. 4) y hay algún vecino que no tiene valoraciones
                if (similarities[index] == Double.POSITIVE_INFINITY || similarities[index]== Double.NEGATIVE_INFINITY)
                    throw new ArithmeticException();
            }
        }catch (ArithmeticException e){
            return recommedationForNewUser(user);
        }

        Enumeration nInstances = neighbors.enumerateInstances();
        Map<String, List<Integer>> recommedations = new HashMap<String, List<Integer>>();

        for(int indexNeighbors = 0; indexNeighbors < neighbors.numInstances(); indexNeighbors++){
            Instance currNeighBor = neighbors.get(indexNeighbors);

            for(int j = 0; j < currNeighBor.numAttributes(); j++){
                if(userData.value(j) < 1){
                    String attrName = userData.attribute(j).name();
                    List<Integer> lst = new ArrayList<Integer>();
                    if(recommedations.containsKey(attrName)){
                        lst = recommedations.get(attrName);
                    }
                    lst.add((int)currNeighBor.value(j));
                    recommedations.put(attrName, lst);
                }
            }
        }

        List<ActivityRecommendationDTO> finalRanks = new ArrayList<ActivityRecommendationDTO>();

        Iterator<String> it = recommedations.keySet().iterator();
        while(it.hasNext()){
            String atrName = it.next();
            double totalImpact = 0;
            double weightedSum = 0;
            List<Integer> ranks = recommedations.get(atrName);
            for(int indexRanks = 0; indexRanks < ranks.size(); indexRanks++){
                int val = ranks.get(indexRanks);
                totalImpact += similarities[indexRanks];
                weightedSum += (double) similarities[indexRanks] * val;
            }

            Activity activity = activityService.getActivityByNameActivity(atrName);

            ActivityRecommendationDTO activityRecommendation = new ActivityRecommendationDTO(activity.getId(), activity.getName(), activity.getDescription(),
                    activity.getCoordenates().getX(), activity.getCoordenates().getY(), activity.getPathImage(), activity.getCity().getNameCity(),
                    activity.getInterest().getNameInterest(), activity.getAddress(), getImageFromActivity(activity),weightedSum/totalImpact, getAverageFromActivity(activity));

            finalRanks.add(activityRecommendation);
        }
        Collections.sort(finalRanks);

        System.out.println(finalRanks.get(0).getName());
        System.out.println(finalRanks.get(1).getName());
        System.out.println(finalRanks.get(2).getName());

        return getSubListRecommedation(finalRanks);
    }

    ResponseEntity<List<ActivityRecommendationDTO>> recommedationForNewUser(User user) throws IOException {
        List<RelUserActivity> relUserActivity = relUserActivityService.getAllValuationByUser(user);
        List<RelUserInterest> relUserInterests = relUserInterestService.getAllPriorityByUser(user);

        List<Activity> activityList = activityService.getActivities();

        List<ActivityRecommendationDTO> finalRanks = new ArrayList<>();
        for(Activity activity: activityList ){

            // search if the activity already is evaluated
            RelUserActivity activityRate = relUserActivity.stream()
                    .filter(relUsAct -> activity.getId() == relUsAct.getActivity().getId())
                    .findAny()
                    .orElse(null);

            if(activityRate == null){
                // get the priority of activity
                RelUserInterest priorityFromUserToInterestOfActivity = relUserInterests.stream()
                        .filter(relUsInt -> activity.getInterest().getId() == relUsInt.getInterest().getId())
                        .findAny()
                        .orElse(null);

                // create the ActivityRecommendation with score with the priority and average
                ActivityRecommendationDTO activityRecommendation = new ActivityRecommendationDTO(activity.getId(), activity.getName(), activity.getDescription(),
                        activity.getCoordenates().getX(), activity.getCoordenates().getY(), activity.getPathImage(), activity.getCity().getNameCity(),
                        activity.getInterest().getNameInterest(), activity.getAddress(), getImageFromActivity(activity), priorityFromUserToInterestOfActivity.getPriority(), getAverageFromActivity(activity));

                //add to the list
                finalRanks.add(activityRecommendation);
            }

        }
        Collections.sort(finalRanks);

        return getSubListRecommedation(finalRanks);

    }

    private ResponseEntity<List<ActivityRecommendationDTO>> getSubListRecommedation(List<ActivityRecommendationDTO> finalRanks) {
        List<ActivityRecommendationDTO> activitiesRecommended;
        try{
            activitiesRecommended = finalRanks.subList(0,9);
        }
        catch (Exception e){
            return new ResponseEntity<List<ActivityRecommendationDTO>>(finalRanks, HttpStatus.OK);
        }

        return new ResponseEntity<List<ActivityRecommendationDTO>>(activitiesRecommended, HttpStatus.OK);
    }

    @PostMapping("/rate")
    public ResponseEntity<?> rateActivity(@Validated @RequestBody ActivityRateByUserDTO activityRateByUserDTO, BindingResult result) {
        if(result.hasErrors()){
            return new ResponseEntity(new Mensaje("Formulario inválido"), HttpStatus.BAD_REQUEST);
        }
        if(!usersService.existsByEmail(activityRateByUserDTO.getEmail_user())){
            return new ResponseEntity(new Mensaje("El usuario con email " + activityRateByUserDTO.getEmail_user() + " no existe"), HttpStatus.NOT_FOUND);
        }
        if(!activityService.existsById(activityRateByUserDTO.getActivity_id())){
            return new ResponseEntity(new Mensaje("La actividad con id " + activityRateByUserDTO.getActivity_id() + " no existe"), HttpStatus.NOT_FOUND);
        }

        User user = usersService.getUserByEmail(activityRateByUserDTO.getEmail_user());
        Activity activity = activityService.getById(activityRateByUserDTO.getActivity_id());

        RelUserActivity relUserActivity = new RelUserActivity();

        relUserActivity.setActivity(activity);
        relUserActivity.setUser(user);
        relUserActivity.setValuation(activityRateByUserDTO.getRate());

        user.getRelUserActivity().add(relUserActivity);

        usersService.updateUser(user);

        return new ResponseEntity(new Mensaje("Actividad puntuada"), HttpStatus.CREATED);
    }

    @GetMapping("/ratedActivities/{id}")
    public ResponseEntity<List<ActivityRecommendationDTO>> getRatedActivities(@PathVariable Long id) throws IOException {
        if(!usersService.existsById(id)){
            return new ResponseEntity(new Mensaje("El usuario con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        }
        User user = usersService.getUserById(id);
        List<RelUserActivity> relUserActivityList = relUserActivityService.getAllValuationByUser(user);

        List<ActivityRecommendationDTO> ratedActivities = new ArrayList<>();
        for(RelUserActivity relUserActivity: relUserActivityList){
            Activity activity = relUserActivity.getActivity();

            ActivityRecommendationDTO activityRecommendationDTO = new ActivityRecommendationDTO(activity.getId(), activity.getName(), activity.getDescription(),
                    activity.getCoordenates().getX(), activity.getCoordenates().getY(), activity.getPathImage(), activity.getCity().getNameCity(), activity.getInterest().getNameInterest(),
                    activity.getAddress(), getImageFromActivity(activity), relUserActivity.getValuation(), getAverageFromActivity(activity));

            ratedActivities.add(activityRecommendationDTO);
        }

        return new ResponseEntity<List<ActivityRecommendationDTO>>(ratedActivities, HttpStatus.OK);
    }

    private double getAverageFromActivity(Activity activity){
        List<RelUserActivity> ratingsActivity = relUserActivityService.getAllValuationByActivity(activity);
        try{

            return Math.floor(ratingsActivity.stream().mapToDouble(RelUserActivity::getValuation).average().getAsDouble() * 100) / 100;
        }catch (NoSuchElementException e){
            return 0;
        }

    }

}
