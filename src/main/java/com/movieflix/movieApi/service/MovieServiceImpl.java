package com.movieflix.movieApi.service;

import com.movieflix.movieApi.dto.MovieDto;
import com.movieflix.movieApi.entities.Movie;
import com.movieflix.movieApi.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService{


    private final MovieRepository movieRepository;

    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        if(Files.exists(Paths.get(path+ File.separator+file.getOriginalFilename()))){
            throw new RuntimeException("File already exists! Please enter another file name");
        }
        String uploadedFileName=fileService.uploadFile(path,file);

        movieDto.setPoster(uploadedFileName);

        Movie movie=new Movie(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );


        Movie savedMovie=movieRepository.save(movie);

        String posterUrl=baseUrl+"/file/"+uploadedFileName;

        MovieDto response=new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl

        );
        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {

        Movie movie=movieRepository.findById(movieId).orElseThrow(()-> new RuntimeException("Movie not found"));

        String posterUrl=baseUrl+"/file/"+movie.getPoster();

        MovieDto response=new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
        return response;
    }

    @Override
    public List<MovieDto> getAllMovies() {

        List<Movie> movies=movieRepository.findAll();

        List<MovieDto> movieDtos=new ArrayList<>();

        //2.Iterate through the list and generate poster url for each of them
        //and map to movieDto object

        for(Movie movie:movies){
            String posterUrl=baseUrl+"/file/"+movie.getPoster();
            MovieDto response=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(response);
        }
        return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer movieId,MovieDto movieDto, MultipartFile file) throws IOException {

        Movie mv=movieRepository.findById(movieId).orElseThrow(
                ()-> new RuntimeException("movie not found"));

        String fileName=mv.getPoster();
        if(file!=null){
            Files.delete(Paths.get(path+File.separator+fileName));
            fileName=fileService.uploadFile(path,file);
        }

        movieDto.setPoster(fileName);

        Movie movie=new Movie(
              movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        Movie savedMovie=movieRepository.save(movie);

        String posterUrl=baseUrl+"/file/"+fileName;

        MovieDto response=new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );
        return response;
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {

        Movie movie=movieRepository.findById(movieId).orElseThrow(
                ()-> new RuntimeException("Movie not found"));


        Files.delete(Paths.get(path+File.separator+movie.getPoster()));

        movieRepository.delete(movie);

        return "movie deleted successfully="+movieId;
    }
}
