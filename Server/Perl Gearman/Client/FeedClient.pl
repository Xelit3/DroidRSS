#!/usr/bin/perl

use strict;
use warnings;
use Gearman::Client;

require FeedDB;

my $feedb= FeedDB->new();
my @channels=@{$feedb->getUrlChannels()};

my $client= new Gearman::Client;
$client->job_servers('192.168.1.133:4730');

foreach(@channels){
	my $result_ref=$client->do_task('parseChannel',$_);
	print $$result_ref;
}
