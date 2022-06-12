package com.courzelo_for_business.job_offers.servicerest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.courzelo_for_business.job_offers.entities.Business;
import com.courzelo_for_business.job_offers.entities.JobOffers;
import com.courzelo_for_business.job_offers.entities.dtos.JobOffersDTO;
import com.courzelo_for_business.job_offers.repositories.JobOffersRepository;
import com.courzelo_for_business.job_offers.servicerest.iservicesrest.IServiceRestJobOffers;


@Service
public class JobOffersRestService implements IServiceRestJobOffers{
	
	@Autowired
    private ModelMapper mapper;
    
    @Autowired
    private  JobOffersRepository jobRepository;
    
    
    @Autowired
    RestTemplateBuilder restTemplateBuilder;
    
    
    @Override
	public List<JobOffersDTO> getAllJobOffers() {
		
		
		List<JobOffers> jobs = jobRepository.findAll();
		jobs.forEach(job->{
			if(job.getBusiness()!=null) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("userId", job.getBusiness().getIdBusiness());
			Business business = restTemplateBuilder.build().getForObject("https://springgateway.herokuapp.com/business-auth/api/auth/{userId}", Business.class, params);
		    job.setBusiness(business);
			}
		});
		return jobs.stream().map(job -> mapper.map(job, JobOffersDTO.class)
		    ).collect(Collectors.toList());
	}
	
    
    public List<JobOffersDTO> getJobOffersByBusiness(String idBusiness){
    	List<JobOffers> jobs = jobRepository.findByBusinessIdBusiness(idBusiness);
    	
    	jobs.forEach(job->{
			if(job.getBusiness()!=null) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("userId", job.getBusiness().getIdBusiness());
			Business business = restTemplateBuilder.build().getForObject("https://springgateway.herokuapp.com/business-auth/api/auth/{userId}", Business.class, params);
		    job.setBusiness(business);
			}
		});
    	
		return jobs.stream().map(job -> mapper.map(job, JobOffersDTO.class))
		.collect(Collectors.toList());
    }
    
    public List<JobOffersDTO> getJobOffersByBusinessAndState(String idBusiness,String state){
    	List<JobOffers> jobs = jobRepository.findByBusinessIdBusinessAndState(idBusiness,state);
    	
    	jobs.forEach(job->{
			if(job.getBusiness()!=null) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("userId", job.getBusiness().getIdBusiness());
			Business business = restTemplateBuilder.build().getForObject("https://springgateway.herokuapp.com/business-auth/api/auth/{userId}", Business.class, params);
		    job.setBusiness(business);
			}
		});
    	
    	
		return jobs.stream().map(job -> mapper.map(job, JobOffersDTO.class))
		.collect(Collectors.toList());
    }
    
    public List<JobOffersDTO> getJobOffersByState(String state){
    	List<JobOffers> jobs = jobRepository.findByState(state);
		return jobs.stream().map(job -> mapper.map(job, JobOffersDTO.class))
		.collect(Collectors.toList());
    }
    
	@Override
	public JobOffersDTO getJobById(String idJob) {
		JobOffers job = jobRepository.findByIdJob(idJob); 
				
		return mapper.map(job, JobOffersDTO.class);
		
		
	}
	
	
   
	public JobOffersDTO addJob(JobOffersDTO requestJob,String idBusiness) {
		
	   JobOffers job = mapper.map(requestJob, JobOffers.class);
	   
	    Map<String, String> params = new HashMap<String, String>();
		params.put("userId", idBusiness);
		Business business = restTemplateBuilder.build().getForObject("https://springgateway.herokuapp.com/business-auth/api/auth/{userId}", Business.class, params);
	    job.setBusiness(business);
	    
	   JobOffers newJob = jobRepository.save(job);
        return mapper.map(newJob, JobOffersDTO.class);
		
	}
    
    
    @Override
	public JobOffersDTO updateJob(String idJob , JobOffersDTO requestJob) {
		
    	JobOffers job = mapper.map(requestJob, JobOffers.class);
		
    	JobOffers thejob = jobRepository.findByIdJob(idJob);
    	thejob.setIdJob(job.getIdJob());
    	thejob.setTitle(job.getTitle());
     	thejob.setDescription(job.getDescription());
     	thejob.setCreationDate(job.getCreationDate());
     	thejob.setDeadlineDate(job.getDeadlineDate());
     	thejob.setStartDate(job.getStartDate());
     	thejob.setIndustry(job.getIndustry());
     	thejob.setSubIndustry(job.getSubIndustry());
     	thejob.setState(job.getState());
     	thejob.setJobType(job.getJobType());
    	thejob.setSchedulesType(job.getSchedulesType());
    	thejob.setLocation(job.getLocation());
    	thejob.setCountry(job.getCountry());
    	thejob.setLocationType(job.getLocationType());
    	thejob.setRequirement(job.getRequirement());
    	thejob.setHireNumber(job.getHireNumber());
    	thejob.setSalaryOption(job.getSalaryOption());
    	thejob.setSalary(job.getSalary());
    	thejob.setSalaryCurrency(job.getSalaryCurrency());
    	thejob.setSalaryRangeMax(job.getSalaryRangeMax());
    	thejob.setSalaryRangeMin(job.getSalaryRangeMin());
    	thejob.setSalaryStartAmout(job.getSalaryStartAmout());
    	thejob.setJobBenefits(job.getJobBenefits());
    	thejob.setCommunication(job.isCommunication());
    	thejob.setCommunicationMails(job.getCommunicationMails());
    	thejob.setIdPrehiringTest(job.getIdPrehiringTest());
    	
    	
    	Map<String, String> params = new HashMap<String, String>();
		params.put("userId", thejob.getBusiness().getIdBusiness());
		Business business = restTemplateBuilder.build().getForObject("https://springgateway.herokuapp.com/business-auth/api/auth/{userId}", Business.class, params);
	    thejob.setBusiness(business);
	    
    	
    	JobOffers newJob = jobRepository.save(thejob);
		
				
		return mapper.map(newJob, JobOffersDTO.class);
		
		
	}
    
    
    @Override
	public void deleteJob(String idJob) {
    	jobRepository.deleteById(idJob);
		
		
		
	}
	

    public JobOffersDTO desactivateJob(String idJob) {
    
    	JobOffers thejob = jobRepository.findByIdJob(idJob);
    	thejob.setState("Inactive");
    	JobOffers newJob = jobRepository.save(thejob);
    	return mapper.map(newJob, JobOffersDTO.class);
   
    }
    
    public JobOffersDTO activateJob(String idJob) {
        
    	JobOffers thejob = jobRepository.findByIdJob(idJob);
    	thejob.setState("Active");
    	JobOffers newJob = jobRepository.save(thejob);
    	return mapper.map(newJob, JobOffersDTO.class);
   
    }
    
    
        public JobOffersDTO assignPrehiringTest(String idJob,String idPrehiringTest) {
        
    	JobOffers thejob = jobRepository.findByIdJob(idJob);
    	thejob.setIdPrehiringTest(idPrehiringTest);
    	JobOffers newJob = jobRepository.save(thejob);
    	return mapper.map(newJob, JobOffersDTO.class);
   
    }
        

        public JobOffersDTO unassignPrehiringTest(String idJob) {
        
    	JobOffers thejob = jobRepository.findByIdJob(idJob);
    	thejob.setIdPrehiringTest("");
    	JobOffers newJob = jobRepository.save(thejob);
    	return mapper.map(newJob, JobOffersDTO.class);
   
    }
        
        
        public JobOffersDTO addTest(String idJob,String idTest) {
            
        	JobOffers thejob = jobRepository.findByIdJob(idJob);
        	if(thejob.getIdTest()==null) {
        		thejob.setIdTest(new ArrayList<String>());
        	}
        	
        	List<String> ids =thejob.getIdTest();
        	ids.add(idTest);
        	thejob.setIdTest(ids);
        	JobOffers newJob = jobRepository.save(thejob);
        	return mapper.map(newJob, JobOffersDTO.class);
       
        }
    


    	public boolean verifTestExist(String idPrehingTest){
    		return jobRepository.existsByIdPrehiringTest(idPrehingTest);
    		
    	}
   
	
}
