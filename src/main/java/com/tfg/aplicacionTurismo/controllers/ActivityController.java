package com.tfg.aplicacionTurismo.controllers;

import com.microsoft.azure.storage.StorageException;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import weka.core.*;
import weka.core.neighboursearch.LinearNNSearch;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Clase que responde a las acciones relacionadas con las actividades.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
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

    @Autowired
    private BlobStorageService blobStorageService;

    /**
     * Método que devuelve la lista de actividades.
     * @return la respuesta HTTP con la lista de actividades.
     * @throws IOException si hay algún fallo en la lectura del archivo desde el disco local.
     * @throws StorageException excepción del servicio de almacenamiento de Azure.
     * @throws InvalidKeyException si la key es inválida.
     * @throws URISyntaxException si la cadena URI no pudo ser parseada ya que no tiene el formato correcto.
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ActivitySendDTO>> getListado() throws IOException, StorageException, InvalidKeyException, URISyntaxException {
        List<Activity> listActivity = activityService.getActivities();
        List<ActivitySendDTO> listDTO = new ArrayList<>();
        for(Activity activity: listActivity){
            ActivitySendDTO i = new ActivitySendDTO(activity.getId(), activity.getName(), activity.getDescription(), activity.getCoordenates().getX(), activity.getCoordenates().getY(), activity.getPathImage(), activity.getCity().getNameCity(), activity.getInterest().getNameInterest(), activity.getAddress(), getImageFromActivity(activity));

            // Add the activity
            listDTO.add(i);
        }
        return new ResponseEntity<List<ActivitySendDTO>>(listDTO, HttpStatus.OK);
    }

    /**
     * Método que devuelve la información de una actividad.
     * @param id identificador de la actividad.
     * @return la respuesta HTTP que contiene un mensaje indicando que la actividad se ha añadido con éxito o
     * la respuesta HTTP que contiene un mensaje de error si no existe una actividad con ese identificador.
     * @throws IOException si hay algún fallo en la lectura del archivo desde el disco local.
     * @throws StorageException excepción del servicio de almacenamiento de Azure.
     * @throws InvalidKeyException si la key es inválida.
     * @throws URISyntaxException si la cadena URI no pudo ser parseada ya que no tiene el formato correcto.
     */
    @GetMapping("/details/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivitySendDTO> getActivity(@PathVariable Long id) throws IOException, StorageException, InvalidKeyException, URISyntaxException {
        if(!activityService.existsById(id)){
            return new ResponseEntity(new Mensaje("La actividad con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        }
        Activity activity = activityService.getById(id);

        ActivitySendDTO activitySendDTO = new ActivitySendDTO(activity.getId(), activity.getName(), activity.getDescription(), activity.getCoordenates().getX(), activity.getCoordenates().getY(), activity.getPathImage(), activity.getCity().getNameCity(), activity.getInterest().getNameInterest(), activity.getAddress(), this.getImageFromActivity(activity));
        return new ResponseEntity<ActivitySendDTO>(activitySendDTO, HttpStatus.OK);
    }

    /**
     * Método que obtiene la imagen de una actividad.
     * @param activity la actividad.
     * @return objeto DTO con la información de la imagen.
     * @throws IOException
     * @throws StorageException
     * @throws InvalidKeyException
     * @throws URISyntaxException
     */
    private ImageDTO getImageFromActivity(Activity activity) throws IOException, StorageException, InvalidKeyException, URISyntaxException {
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
        /*byte[] fileContent = blobStorageService.getFile(activity.getPathImage()).toByteArray();*/
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        imageDTO.setData(encodedString);



        return imageDTO;
    }

    /**
     * Método que añade una nueva actividad.
     * @param multipartFile la imagen.
     * @param activityDTO objeto DTO que contiene la información necesaria para añadir una nueva actividad.
     * @param result parámetro que permite validar los errores en el objeto dto.
     * @return la respuesta HTTP que contiene un mensaje indicando que la actividad se ha añadido con éxito o
     * la respuesta HTTP que contiene un mensaje de error si no incluye una imagen, si ya existe una actividad con el nombre de la nueva actividad,
     * si los datos no son correctos, si la ciudad no existe o si el tipo de interés no existe.
     * @throws IOException si hay algún fallo en la lectura del archivo desde el disco local.
     * @throws URISyntaxException si la cadena URI no pudo ser parseada ya que no tiene el formato correcto.
     * @throws StorageException excepción del servicio de almacenamiento de Azure.
     * @throws InvalidKeyException si la key es inválida.
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addActivity(@RequestParam(name="image", required = false) MultipartFile multipartFile, @Validated ActivityDTO activityDTO, BindingResult result) throws IOException, URISyntaxException, StorageException, InvalidKeyException {
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
        // Remove the accent
        fileName = stripDiacritics(fileName);
        // Remove the whitespaces
        fileName = fileName.toLowerCase().replaceAll(" ", "-");

        Activity activity = new Activity(activityDTO.getName(), activityDTO.getDescription(), new Point(activityDTO.getLongitude(), activityDTO.getLatitude()), fileName, activityDTO.getAddress());
        City city = cityService.getCityByNameCity(activityDTO.getCity());
        activity.setCity(city);
        Interest interest = interestService.getInterestByName(activityDTO.getInterest());
        activity.setInterest(interest);
        activityService.addActivities(activity);

        String folder = "/Users/alba-/Desktop/photos/";
        FileUploadUtil.saveFile(folder, fileName, multipartFile);
        //blobStorageService.upload(fileName, multipartFile.getInputStream(), multipartFile.getSize());

        return new ResponseEntity<>(new Mensaje("Actividad creada"), HttpStatus.CREATED);
    }

    public static final Pattern DIACRITICS_AND_FRIENDS
            = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

    /**
     * Método que elimina los acentos de un texto.
     * @param str el texto
     * @return el texto sin acentos.
     */
    private static String stripDiacritics(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = DIACRITICS_AND_FRIENDS.matcher(str).replaceAll("");
        return str;
    }

    /**
     * Método qye elimina una actividad a través de su identificador.
     * @param id identificador de la actividad que se quiere eliminar.
     * @return la respuesta HTTP que contiene un mensaje indicando que la actividad se ha eliminado con éxito o
     * la respuesta HTTP que contiene un mensaje de error si no existe una actividad con ese identificador.
     * @throws InvalidKeyException si la key es inválida.
     * @throws StorageException excepción del servicio de almacenamiento de Azure.
     * @throws URISyntaxException si la cadena URI no pudo ser parseada ya que no tiene el formato correcto.
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteActivity(@PathVariable Long id) throws InvalidKeyException, StorageException, URISyntaxException {
        if(!activityService.existsById(id)){
            return new ResponseEntity<>(new Mensaje("La actividad con id " + id + " no existe"), HttpStatus.NOT_FOUND);
        }
        //NOTE: Comprobar que no tiene usuarios asociados
        Activity activity = activityService.getById(id);
        String pathImage = "/Users/alba-/Desktop/photos/" + activity.getPathImage();
        FileUploadUtil.removeFile(pathImage);
        /*blobStorageService.deleteFile(activity.getPathImage());*/
        activityService.removeActivities(id);

        return new ResponseEntity<>(new Mensaje("Actividad eliminada"), HttpStatus.OK);
    }

    /**
     * Método que modifica la información de una actividad a través de su identificador.
     * @param multipartFile la imagen
     * @param activityDTO objeto DTO que contiene la información necesaria para actualizar una actividad.
     * @param result parámetro que permite validar los errores en el objeto dto.
     * @param id identificador de la actividad.
     * @return la respuesta HTTP que contiene un mensaje indicando que la actividad se ha actualizado con éxito o
     * la respuesta HTTP que contiene un mensaje de error si no incluye una imagen, si ya existe una actividad con el nuevo nombre,
     * si los datos no son correctos, si la ciudad no existe o si el tipo de interés no existe.
     * @throws IOException si hay algún fallo en la lectura del archivo desde el disco local.
     * @throws StorageException excepción del servicio de almacenamiento de Azure.
     * @throws InvalidKeyException si la key es inválida.
     * @throws URISyntaxException si la cadena URI no pudo ser parseada ya que no tiene el formato correcto.
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateActivity(@RequestParam(name = "image", required = false) MultipartFile multipartFile, @Validated ActivityDTO activityDTO,BindingResult result, @PathVariable Long id) throws IOException, StorageException, InvalidKeyException, URISyntaxException {
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
        // Remove the accent
        fileName = stripDiacritics(fileName);
        // Remove the whitespaces
        fileName = fileName.toLowerCase().replaceAll(" ", "-");
        activity.setPathImage(fileName);

        //Eliminamos el archivo
        String downloadDir = "/Users/alba-/Desktop/photos/" + previousPath;
        FileUploadUtil.removeFile(downloadDir);
        //blobStorageService.deleteFile(previousPath);

        //Añadimos el nuevo archivo
        String uploadDir = "/Users/alba-/Desktop/photos/";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        //blobStorageService.upload(fileName, multipartFile.getInputStream(), multipartFile.getSize());

        activityService.updateActivity(activity);

        return new ResponseEntity<>(new Mensaje("Actividad actualizada"), HttpStatus.CREATED);
    }

    /**
     * Método que devuelve la lista de actividades recomendadas a un usuario.
     * @param id identificador del usuario.
     * @return respuesta HTTP con la lista de actividades recomendadas
     * o la respuesta HTTP que contiene un mensaje de error si el usuario no existe.
     * @throws IOException si hay algún fallo en la lectura del archivo desde el disco local.
     * @throws StorageException excepción del servicio de almacenamiento de Azure.
     * @throws InvalidKeyException si la key es inválida.
     * @throws URISyntaxException si la cadena URI no pudo ser parseada ya que no tiene el formato correcto.
     */
    @GetMapping("/recommedation/{id}")
    public ResponseEntity<List<ActivityRecommendationDTO>> getRecommendedActivities(@PathVariable Long id) throws IOException, StorageException, InvalidKeyException, URISyntaxException {
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

        /*System.out.println(finalRanks.get(0).getName());
        System.out.println(finalRanks.get(1).getName());
        System.out.println(finalRanks.get(2).getName());*/

        return getSubListRecommedation(finalRanks);
    }

    /**
     * Método que obtiene actividades recomendadas para un nuevo usuario.
     * @param user usuario.
     * @return lista de actividades recomendadas.
     * @throws IOException
     * @throws StorageException
     * @throws InvalidKeyException
     * @throws URISyntaxException
     */
    ResponseEntity<List<ActivityRecommendationDTO>> recommedationForNewUser(User user) throws IOException, StorageException, InvalidKeyException, URISyntaxException {
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

    /**
     * Método que devuelve una sublista de la lista de actividades recomendadas.
     * @param finalRanks lista de actividades recomendadas.
     * @return sublista de las actividades recomendadas.
     */
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

    /**
     * Método que añade una nueva valoración a una actividad.
     * @param activityRateByUserDTO objeto DTO que contiene la información necesaria para valorar una actividad.
     * @param result parámetro que permite validar los errores en el objeto dto.
     * @return respuesta HTTP con un mensaje indicando que la actividad se ha valorado con éxito o
     * o la respuesta HTTP que contiene un mensaje de error si los datos no son correctos, si no existe el usuario
     * o si la actividad no existe.
     */
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

    /**
     * Método que obtiene la lista de actividades valoradas por un usuario.
     * @param id identificador del usuario.
     * @return respuesta HTTP con la lista de actividades valoradas por el usuario
     * o la respuesta HTTP que contiene un mensaje de error si los datos no son correctos.
     * @throws IOException si hay algún fallo en la lectura del archivo desde el disco local.
     * @throws StorageException excepción del servicio de almacenamiento de Azure.
     * @throws InvalidKeyException si la key es inválida.
     * @throws URISyntaxException si la cadena URI no pudo ser parseada ya que no tiene el formato correcto.
     */
    @GetMapping("/ratedActivities/{id}")
    public ResponseEntity<List<ActivityRecommendationDTO>> getRatedActivities(@PathVariable Long id) throws IOException, StorageException, InvalidKeyException, URISyntaxException {
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

    /**
     * Método que calcula la media de las valoraciones dadas a una actividad.
     * @param activity actividad
     * @return media de las valoraciones de una actividad.
     */
    private double getAverageFromActivity(Activity activity){
        List<RelUserActivity> ratingsActivity = relUserActivityService.getAllValuationByActivity(activity);
        try{

            return Math.floor(ratingsActivity.stream().mapToDouble(RelUserActivity::getValuation).average().getAsDouble() * 100) / 100;
        }catch (NoSuchElementException e){
            return 0;
        }

    }

}
